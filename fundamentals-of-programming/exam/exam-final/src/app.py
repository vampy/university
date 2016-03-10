'''
Created on Jan 30, 2014

@author: daniel
'''
from repository.word import WordsRepository
from controller.spellchecker import SpellCheckerController
from domain.wordvalidator import WordValidator, WordValidatorException
from ui.ui import SpellChekerUI

class SpellCheckerApp(object):
    def __init__(self):
        self._words_repository = WordsRepository("dictionary.dat")
        self._words_validator = WordValidator()
        self._spellc_controller = SpellCheckerController(self._words_repository, self._words_validator)
        
        self._ui = SpellChekerUI(self._spellc_controller, "SpellchecAker App")
    
    def main(self):
        """
        Execute the main program
        """
        self._ui.run()
        