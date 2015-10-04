#!/usr/bin/env python3
# -*- coding: utf-8 -*-


import urllib.parse
import sys
import os
import requests
import argparse

from bs4 import BeautifulSoup
import xlrd

DIR_UNDERGRADUATE = 'dl_undergraduate_documents'
DIR_MASTER = 'dl_master_documents'
URL = 'http://www.cs.ubbcluj.ro/repartizarea-studentilor-pe-grupe-pentru-anul-universitar-2015-2016/'
ID_UNDERGRADUATE = 'http://www.cs.ubbcluj.ro/files/studenti/2015/licenta'
ID_MASTER = 'http://www.cs.ubbcluj.ro/files/studenti/2015/master'


def download_in_dir(docs, parent_dir, force_downloads=False):
    for href, doc_filename in docs:
        local_path = os.path.join(parent_dir, doc_filename)
        if os.path.exists(local_path) and not force_downloads:
            print('File exists: {0}'.format(doc_filename))
            continue

        r = requests.get(href)
        print('Saving: {0}'.format(doc_filename))
        with open(local_path, 'wb') as f:
            f.write(r.content)


def process_excel(docs, parent_dir):
    total = 0
    for href, doc_filename in docs:
        doc = xlrd.open_workbook(os.path.join(parent_dir, doc_filename))

        # Should only have Sheet 1 with data
        for i in range(doc.nsheets):
            sheet = doc.sheet_by_index(i)
            if sheet.nrows and sheet.ncols:
                if i != 0:
                    print('WARNING: Sheet {0} has data in the document {1}'.format(i, doc_filename))
                    continue

                found_row = None

                # Try to find our number row on the first column
                for row in range(sheet.nrows):
                    cell = sheet.cell(row, 0)
                    if xlrd.XL_CELL_TEXT == cell.ctype and cell.value.lstrip().lower().startswith('nr'):
                        found_row = row
                        break

                    # Some stupid documents do not have the Nr. Crt. at the start, sigh, use the next cell
                    next_cell = sheet.cell(row, 1)
                    if xlrd.XL_CELL_TEXT == next_cell.ctype and next_cell.value.lstrip().lower().startswith('matricol'):
                        found_row = row
                        break

                if found_row is None:
                    print('Could not find row number for {0}'.format(doc_filename))
                    continue

                # Assume that nr, is one of the last rows
                for row in reversed(range(sheet.nrows)):
                    cell = sheet.cell(row, 0)
                    if xlrd.XL_CELL_NUMBER == cell.ctype:
                        found = int(cell.value)
                        print('Found for {0} = {1}'.format(doc_filename, found))
                        total += found
                        break

    return total


def main(force_downloads=False):
    # Get html page
    print('Getting whole page')
    r = requests.get(URL)
    if r.status_code != 200:
        sys.exit('Failed to download, status code = {0}'.format(r.status_code))

    # HTML parser
    print('Parsing HTML page')
    soup = BeautifulSoup(r.text, 'html.parser')

    # Parse links
    print('Parsing links from HTML page')
    docs_undergraduate = []
    specialities_total_undergraduate = {}  # map from speciality name to (total students, group number)
    specialities_map_undergraduate = {}  # map from group name to speciality
    docs_master = []
    for link in soup.find_all('a'):
        href = urllib.parse.unquote(link.get('href').strip())
        found = False
        if href.startswith(ID_UNDERGRADUATE):
            # speciality = link.find_previous('h3')
            # specialities_total_undergraduate[link.string] =
            docs_undergraduate.append((href, link.string))
        elif href.startswith(ID_MASTER):
            # speciality = link.find_previous('h3')
            # print(str(link.find_previous('h3')))
            docs_master.append((href, link.string))

    # return
    # Create docs sub directories
    path_current = os.getcwd()
    path_undergraduate_documents = os.path.join(path_current, DIR_UNDERGRADUATE)
    if not os.path.exists(path_undergraduate_documents):
        os.makedirs(path_undergraduate_documents)

    path_master_documents = os.path.join(path_current, DIR_MASTER)
    if not os.path.exists(path_master_documents):
        os.makedirs(path_master_documents)

    # Download all documents
    print('Downloading all documents\n')
    download_in_dir(docs_undergraduate, path_undergraduate_documents, force_downloads)
    download_in_dir(docs_master, path_master_documents, force_downloads)

    # Process excel
    print('\nProcessing Excel')
    total_undergraduate = process_excel(docs_undergraduate, path_undergraduate_documents)
    total_undergraduate_groups = len(docs_undergraduate)
    total_master = process_excel(docs_master, path_master_documents)
    total_master_groups = len(docs_master)
    total_groups = total_master_groups + total_undergraduate_groups
    total_students = total_undergraduate + total_master

    print('\n\nGroups undergraduate = {0}'.format(total_undergraduate_groups))
    print('Students undergraduate = {0}'.format(total_undergraduate))
    print('Average per group = {0}'.format(total_undergraduate // total_undergraduate_groups))

    print('\nGroups master = {0}'.format(total_master_groups))
    print('Students master = {0}'.format(total_master))
    print('Average per group = {0}'.format(total_master // total_master_groups))

    print('\nTotal groups = {0}'.format(total_groups))
    print('Total students = {0}'.format(total_students))
    print('Overall average = {0}'.format(total_students // total_groups))


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='A program that counts students')
    parser.add_argument('-f', '--force', dest='force_downloads', action='store_true', help='Force file downloads')
    parser.set_defaults(force_downloads=False)
    result = parser.parse_args()
    main(result.force_downloads)
