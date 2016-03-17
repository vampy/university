'''
Created on Jan 30, 2014

@author: daniel
'''
from domain.word import Word
from domain.wordvalidator import WordValidatorException


class SpellCheckerController(object):
    def __init__(self, word_repository, word_validator):
        self._word_repository = word_repository
        self._word_validator = word_validator

    def add_word(self, word_id, word_lang, word_word):
        """
        Add a word to the dictionary
        Input:
            word_id - the id of the word
            word_lang - the language of the word
            word_word - the word itself
        Return:
            The message of the status
        """
        try:
            word = Word(word_id, word_lang, word_word)
            self._word_validator.validate(word)
        except WordValidatorException as e:
            return e.message

        if self._word_repository.find(word_lang, word_word) is None:
            self._word_repository.add(word)
            return "Word added"
        else:
            return "That word already exists"

    def check_phrase(self, lang, text):
        """
        Check a phrase against the dictionary
        Input:
            lang - the language of the phrase
            text - the phrase
        Return:
            An invalid message if wrong language
            Or
            The list of all words that are not in the dictionary 
        """
        if lang not in ["En", "Ro", "Fr"]:
            return "Language invalid"

        return_list = []

        for word in text.split(" "):
            if self._word_repository.find(lang, word) is None:
                return_list.append(word)

        return return_list

    def check_filename(self, in_filename, lang, out_filename):
        """
        Check a file against the dictionary
        Input:
            in_filename - the input  filename
            lang - the language of the file
            out_filename - the output filename
        Return:
            An invalid message if wrong language
            Or
            The list of all words that are not in the dictionary 
        """
        if lang not in ["En", "Ro", "Fr"]:
            return "Language invalid"

        try:
            with open(in_filename, "r") as fp_r:
                # file for writing
                fp_w = open(out_filename, "w")

                # read every line from 
                for line in fp_r:

                    # get all the words
                    words = line.strip().split(" ")

                    # prepare to write
                    print_line = ""
                    for word in words:

                        # find out if in the dictionary
                        if len(word) > 2:
                            if self._word_repository.find(lang, word) is None:
                                print_line += "{" + word + "}" + " "
                            else:
                                print_line += word + " "
                        else:  # smaller than 2 insert as usual
                            print_line += word + " "

                    # print the line
                    fp_w.write(print_line + "\n")

                fp_w.close()

            return "File written"
        except IOError as e:
            return "Error on reading file " + e.message
