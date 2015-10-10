#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import urllib.parse
import sys
import argparse
import time

import re
import os
import requests
from collections import namedtuple
from bs4 import BeautifulSoup
import xlrd

PATH_DOWNLOAD = 'dl'
PATH_WEBPAGE = os.path.join(PATH_DOWNLOAD, 'page.html')
PATH_UNDERGRADUATE = os.path.join(PATH_DOWNLOAD, 'undergraduate')
PATH_GRADUATE = os.path.join(PATH_DOWNLOAD, 'graduate')

CACHE_KEY_PAGE = 'page'
CACHE_EXPIRES_SECONDS = 3600 * 24  # 1 day

LOCATION_DOWNLOAD = 'http://www.cs.ubbcluj.ro/repartizarea-studentilor-pe-grupe-pentru-anul-universitar-2015-2016/'
ID_UNDERGRADUATE = 'http://www.cs.ubbcluj.ro/files/studenti/2015/licenta'
ID_MASTER = 'http://www.cs.ubbcluj.ro/files/studenti/2015/master'

Document = namedtuple('Document', ['href', 'filename', 'speciality'])


class SpecialityType:
    def __init__(self):
        self.total = 0  # Total number of students in all users
        self.years = {}  # Map from int (which represents the year) to total students in that year, eg: 1 => 100
        self.language = None


def is_cache_expired(file_mtime, expires_time=CACHE_EXPIRES_SECONDS):
    return int(time.time() - file_mtime) >= expires_time


def get_group_from_filename(filename):
    # eg: 'grupa 246 - 2015-2016.xls'
    return filename.split(' ')[1]


def get_year_from_group(group, is_undergraduate):
    year_indicator = int(group[1])

    # undergraduate just indicates the year; eg: 213 (year 1)
    if is_undergraduate:
        return year_indicator

    # graduate, normalize year; eg: 258 (year 2, because 5 - 3 = 2)
    return year_indicator - 3


def get_header(string, margin=20):
    return '\n{0} {1} {0}'.format('=' * margin, string)


def download_in_dir(docs, parent_dir, force_downloads=False, is_verbose=True):
    for href, filename, speciality in docs:
        local_path = os.path.join(parent_dir, filename)

        # File is already downloaded in our cache.
        if os.path.exists(local_path) and not force_downloads and not is_cache_expired(os.path.getmtime(local_path)):
            if is_verbose:
                print('File is cached: "{0}"'.format(filename))
            continue

        r = requests.get(href)
        if is_verbose:
            print('Saving: "{0}"'.format(filename))
        with open(local_path, 'wb') as f:
            f.write(r.content)


def process_excel(docs, parent_dir, specialities_total, is_undergraduate, is_verbose=True):
    total = 0
    pattern_find_language = re.compile(r'((?:rom|mag|eng|ger).*(?:ă|a))\)?', re.IGNORECASE | re.UNICODE)
    for href, filename, speciality in docs:
        doc = xlrd.open_workbook(os.path.join(parent_dir, filename))

        # Should only have 'Sheet 1' with data
        for i in range(doc.nsheets):
            sheet = doc.sheet_by_index(i)
            if sheet.nrows and sheet.ncols:
                # Ignore all other sheets and report anomaly
                if i != 0:
                    print('WARNING: Sheet {0} has data in the document "{1}"'.format(i, filename))
                    continue

                found_row = None
                speciality_name_alt = None

                # Try to find our number row on the first column
                for row in range(sheet.nrows):
                    cell = sheet.cell(row, 0)
                    if xlrd.XL_CELL_TEXT == cell.ctype:
                        cell_value = cell.value.lstrip().lower()

                        # Found 'Sectia: ...' or 'Specializare...'
                        if cell_value.startswith('sec') or cell_value.startswith('spe'):
                            speciality_name_alt = cell.value.strip()

                        # Found 'Nr. Crt.' columns
                        if cell_value.startswith('nr'):
                            found_row = row
                            break

                    # Some stupid documents do not have the 'Nr. Crt.' at the start, sigh, use the next cell
                    next_cell = sheet.cell(row, 1)
                    if xlrd.XL_CELL_TEXT == next_cell.ctype and next_cell.value.lstrip().lower().startswith('matricol'):
                        found_row = row
                        break

                if speciality_name_alt is None:
                    print('Could not find speciality for "{0}"'.format(filename))

                if found_row is None:
                    print('Could not find row number for "{0}"'.format(filename))
                    continue

                # Assume that 'nr', is one of the last rows
                for row in reversed(range(sheet.nrows)):
                    cell = sheet.cell(row, 0)
                    if xlrd.XL_CELL_NUMBER == cell.ctype:
                        found = int(cell.value)
                        if is_verbose:
                            print('Found for "{0}" = {1}'.format(filename, found))

                        # Overall total
                        total += found

                        # Total by speciality
                        specialities_total.setdefault(speciality, SpecialityType()).total += found
                        speciality_type = specialities_total[speciality]

                        # Find language from the excel 'Section name'
                        if speciality_name_alt is not None:
                            found_language = re.findall(pattern_find_language, speciality_name_alt)
                            if found_language:
                                speciality_type.language = found_language[0]

                        # Total by year in speciality
                        group = get_group_from_filename(filename)
                        year = get_year_from_group(group, is_undergraduate)
                        speciality_type.years.setdefault(year, 0)
                        speciality_type.years[year] += found

                        break

    return total


def main(path_root, force_downloads=False, is_verbose=True):
    if not is_verbose:
        print('Running...')

    path_webpage = os.path.join(path_root, PATH_WEBPAGE)

    # Create docs sub directories
    path_undergraduate = os.path.join(path_root, PATH_UNDERGRADUATE)
    if not os.path.exists(path_undergraduate):
        os.makedirs(path_undergraduate)

    path_graduate = os.path.join(path_root, PATH_GRADUATE)
    if not os.path.exists(path_graduate):
        os.makedirs(path_graduate)

    # Get html page
    # Get from cache
    if os.path.exists(path_webpage) and not force_downloads and not is_cache_expired(os.path.getmtime(path_webpage)):
        if is_verbose:
            print('Webpage is cached')
        with open(path_webpage, 'r') as f:
            page_html = f.read()

    else:  # Get from the interwebz
        if is_verbose:
            print('Getting webpage')
        r = requests.get(LOCATION_DOWNLOAD)
        if r.status_code != 200:
            sys.exit('Failed to download, status code = {0}'.format(r.status_code))
        page_html = r.text

        # Save to cache
        with open(path_webpage, 'w') as f:
            f.write(page_html)

    # HTML parser
    if is_verbose:
        print('Parsing HTML page')
    soup = BeautifulSoup(page_html, 'html.parser')

    # Parse links
    if is_verbose:
        print('Parsing links from HTML page')
    docs_undergraduate = []
    specialities_total = {}  # map from speciality name to SpecialityType
    docs_master = []
    for link in soup.find_all('a'):
        href = urllib.parse.unquote(link.get('href').strip())
        if href.startswith(ID_UNDERGRADUATE):
            speciality = link.find_previous('h3').string
            docs_undergraduate.append(Document(href, filename=link.string, speciality=speciality))
        elif href.startswith(ID_MASTER):
            speciality = link.find_previous('h3').string
            docs_master.append(Document(href, filename=link.string, speciality=speciality))

    # Download all documents
    if is_verbose:
        print('\nDownloading all documents')
    download_in_dir(docs_undergraduate, path_undergraduate, force_downloads, is_verbose=is_verbose)
    download_in_dir(docs_master, path_graduate, force_downloads, is_verbose=is_verbose)

    # Process excel
    if is_verbose:
        print('\nProcessing Excel')
    total_undergraduate = process_excel(docs_undergraduate, path_undergraduate, specialities_total, is_undergraduate=True,
                                        is_verbose=is_verbose)
    total_undergraduate_groups = len(docs_undergraduate)
    total_master = process_excel(docs_master, path_graduate, specialities_total, is_undergraduate=False, is_verbose=is_verbose)
    total_master_groups = len(docs_master)
    total_groups = total_master_groups + total_undergraduate_groups
    total_students = total_undergraduate + total_master

    print(get_header('Stats by total groups/students'))
    print('Groups undergraduate = {0}'.format(total_undergraduate_groups))
    print('Students undergraduate = {0}'.format(total_undergraduate))
    print('Average per group = {0}'.format(total_undergraduate // total_undergraduate_groups))

    print('\nGroups master = {0}'.format(total_master_groups))
    print('Students master = {0}'.format(total_master))
    print('Average per group = {0}'.format(total_master // total_master_groups))

    print('\nTotal groups = {0}'.format(total_groups))
    print('Total students = {0}'.format(total_students))
    print('Overall average = {0}'.format(total_students // total_groups))

    print(get_header('Stats by speciality'))
    for speciality in sorted(specialities_total.keys()):
        speciality_type = specialities_total[speciality]

        # Add language
        name = speciality
        if speciality_type.language:

            # Remove language if found, add our language below
            found_index = name.find(speciality_type.language[:3])
            if found_index != -1:
                name = name[:found_index - 1]

            name += ' - ' + speciality_type.language

        years = []
        for year in speciality_type.years:
            years.append('"Year {0}" = {1}'.format(year, speciality_type.years[year]))

        print('{0}\n\tTotal = {1}\n\t{2}\n'.format(name, speciality_type.total, "\n\t".join(years)))


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='A program that counts students')
    parser.add_argument('-f', '--force', action='store_true', help='force file downloads')
    parser.add_argument('-c', '--clean', action='store_true', help='clean downloads directory')
    parser.add_argument("-v", "--verbose", action="store_true", help="increase output verbosity")
    parser.set_defaults(force_downloads=False)
    args = parser.parse_args()

    abs_path_root = os.getcwd()
    if args.clean:
        path_clean = os.path.join(abs_path_root, PATH_DOWNLOAD)
        if os.path.exists(path_clean):
            import shutil

            print('Cleaning download directory')
            shutil.rmtree(path_clean)
        else:
            print('Download directory does not exist: "{0}"'.format(path_clean))
    else:
        main(path_root=abs_path_root, force_downloads=args.force, is_verbose=args.verbose)
