'''
Created on Jan 30, 2014

@author: daniel
'''
from domain.word import Word


class WordsRepositoryException(Exception):
    pass


class WordsRepository(object):
    def __init__(self, file_name="dictionary.dat"):
        self._file_name = file_name
        self._repository = {}  # {"En": [the list of objects]}
        self._load_file()

    def find(self, lang, word):
        """
        Find a word by lang in the repository
        Input:
            lang - a string representing the language
            word - a string representing the word
        Return:
            An instance of type word if found
            None otherwise
        """
        # category not define
        if lang not in self._repository:
            return None

        # return the instance if found
        for w in self._repository[lang]:
            if w.word == word:
                return w

        return None

    def _add_to_memory(self, word):
        """
        Add the word to memory
        Input:
            word - an instance of type word
        Raise:
            WordsRepositoryException on invalid input
        """
        if not isinstance(word, Word):
            raise WordsRepositoryException("Word is not of type Word")

        # already in list
        if word.lang in self._repository:
            self._repository[word.lang].append(word)
        else:
            self._repository[word.lang] = [word]

    def add(self, word):
        """
        Add the memory and to the file
        Input:
            word - an instance of type word
        Raise:
            WordsRepositoryException on invalid input
        """
        self._add_to_memory(word)

        # write to file
        with open(self._file_name, "a") as fp:
            print("Saving to file")

            fp.write(str(word.id) + " " + word.lang + " " + word.word + "\n")

    def get_all(self):
        """
        Get the repository
        Return:
            a dictionary with keys as the language and the values as a list
            of words
        """
        return self._repository

    def _load_file(self):
        """
        Load the repository from the file
        """
        try:
            with open(self._file_name, "r") as fp:
                for line in fp:
                    line_parsed = line.strip().split(" ")

                    word_id = line_parsed[0]
                    word_lang = line_parsed[1]
                    word_word = line_parsed[2]

                    # create the instance
                    word = Word(word_id, word_lang, word_word)
                    #                     print word

                    # add it
                    self._add_to_memory(word)

        except IOError:
            # file is not present
            with open(self._file_name, "w") as fp:
                print("File is not present making a new one")
