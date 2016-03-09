#!/usr/bin/python

from ui.ui import UI
from domain.session import Session

from repository.fileclient import FileClientRepository
from repository.filemovie import FileMovieRepository
from repository.filerent import FileRentRepository
from controller.client import ClientController
from controller.movie import MovieController
from controller.rent import RentController


class App(object):
    def __init__(self, first_message=""):
        """
        The construction for the app. Used for reading the bloc from the file
        """
        self.client_repository = FileClientRepository("clients_new.dat")
        self.movie_repository = FileMovieRepository("movies_new.dat")
        self.rent_repository = FileRentRepository("rents_new.dat")

        self.client_controller = ClientController(self.client_repository)
        self.movie_controller = MovieController(self.movie_repository)
        self.rent_controller = RentController(rent_repository=self.rent_repository,
                                              client_controller=self.client_controller,
                                              movie_controller=self.movie_controller)

        self.ui = UI(self.client_controller, self.movie_controller, self.rent_controller)
        Session.set_message(first_message)

    def run(self):
        """
        Main loop of the application
        """
        #self.load()
        self.ui.display_message()
        self.ui.run()
        #self.save()
        self.exit()

    #def load(self):
    #    """
    #    Load the previous state
    #    """
    #    try:
    #        with open("clients.dat", "r") as fp:
    #            self.client_repository._repository = cPickle.load(fp)
    #    except cPickle.UnpicklingError:
    #        pass
    #    except IOError:
    #        pass
    #
    #    try:
    #        with open("movies.dat", "r") as fp:
    #            self.movie_repository._repository = cPickle.load(fp)
    #    except cPickle.UnpicklingError:
    #        pass
    #    except IOError:
    #        pass
    #
    #    try:
    #        with open("rents.dat", "r") as fp:
    #            self.rent_repository._repository = cPickle.load(fp)
    #    except cPickle.UnpicklingError:
    #        pass
    #    except IOError:
    #        pass
    #
    #def save(self):
    #    """
    #    Saves the current state
    #    """
    #    with open("clients.dat", "w") as fp:
    #        cPickle.dump(self.client_repository._repository, fp)
    #    with open("movies.dat", "w") as fp:
    #        cPickle.dump(self.movie_repository._repository, fp)
    #    with open("rents.dat", "w") as fp:
    #        cPickle.dump(self.rent_repository._repository, fp)

    @staticmethod
    def exit():
        """
        Quits the app, called on 'quit' command
        """
        exit("\nBye Bye :)")