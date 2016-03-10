'''
Created on Jan 30, 2014

@author: daniel
'''
from repository.word import WordsRepository, WordsRepositoryException
from controller.spellchecker import SpellCheckerController
from domain.word import Word
from domain.wordvalidator import WordValidator, WordValidatorException

def test_validator():
    word = Word("2", "Ro", "romana")
    validator = WordValidator()
    
    try:
        validator.validate(word)
        assert True
    except WordValidatorException:
        assert False
        
    word.word = ""
    try:
        validator.validate(word)
        assert False
    except WordValidatorException:
        assert True
        word.word = "nume"
        
    word.lang = ""
    try:
        validator.validate(word)
        assert False
    except WordValidatorException:
        assert True
        word.lang = "Ro"
        
    word.lang = "Rou"
    try:
        validator.validate(word)
        assert False
    except WordValidatorException:
        assert True
        
def test_spellc_controller_repository():
    repository = WordsRepository("test_dictionary.dat")
    validator = WordValidator()
    controller = SpellCheckerController(repository, validator)
    
    word = Word("2", "Ro", "mere")
    try:
        repository._add_to_memory(word)
        assert True
    except WordsRepositoryException:
        assert False
        
    try:
        repository._add_to_memory("str")
        assert False
    except WordsRepositoryException:
        assert True
        
    assert repository.find("Fr", "romana") == None
    assert repository.find("Br", "romana") == None
    
    assert controller.add_word("3", "Ro", "mere") == "That word already exists"
    assert controller.add_word("3", "Ro", "mama") == "Word added"
    
    
if __name__ == "__main__":
    test_validator()
    test_spellc_controller_repository()