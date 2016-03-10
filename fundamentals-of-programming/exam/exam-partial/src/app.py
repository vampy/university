from repository.pharmacy import PharmacyRepo
from controller.pharmacy import PharmacyController
from ui.ui import PharmacyUI

class PharmacyApp(object):
    def __init__(self):
        pharma_repository = PharmacyRepo()
        pharma_controller = PharmacyController(pharma_repository)
        self.__ui = PharmacyUI(pharma_controller)

    
    def run(self):
        """
        Run the main Application
        """
        self.__ui.run()

if __name__ == "__main__":
    app = PharmacyApp()
    app.run()
        
    