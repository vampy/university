'''
Created on Jan 30, 2014

@author: daniel
'''
from domain.word import Word


class WordValidatorException(Exception):
    pass


class WordValidator(object):
    def __init__(self):
        pass
    
    def validate(self, word):
        """
        Validate an word instance
        Input:
            word - a instance of type word
        Raises:
            WordValidatorException - on empty invalid word
        """
        if not isinstance(word, Word):
            raise WordValidatorException("Not of type word")
        
        if not word.lang:
            raise WordValidatorException("Language  field can not be empty")

        if not word.id:  
            raise WordValidatorException("Id field can not be empty")   
        
        if not word.word:  
            raise WordValidatorException("Word field can not be empty")   
        
        if " " in word.word:
            raise WordValidatorException("Word field must be formed by only a word")
        
        try:
            temp_id = int(word.id)
        except ValueError:
            raise WordValidatorException("Id field is not an integer")
            
        if word.lang not in ["En", "Ro", "Fr"]:
            raise WordValidatorException("Language is not in the list of approved languages")