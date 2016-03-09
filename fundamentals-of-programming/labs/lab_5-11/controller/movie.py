#!/usr/bin/python
from controller import Controller
from domain.movie import Movie
from domain.session import Session
from repository.repository import RepositoryException
import copy


class MovieController(Controller):
    def store(self, movie_id, movie_title, movie_type, movie_description):
        """
        Store a new movie
        Time complexity:
            O(1)
        Input:
            instance - an object
        Output:
            True - success
            False - failure
        Raises:
        """
        movie_obj = Movie(movie_id=movie_id, movie_title=movie_title, movie_type=movie_type, movie_description=movie_description)
        try:
            self._repository.insert(movie_obj)
        except RepositoryException as e:
            Session.set_message(e.message)
            return False

        return True

    def update(self, movie_id, movie_title="", movie_type="", movie_description=""):
        """
        Update a movie by id
        Time complexity:
            O(1)
        Input:
            instance - an object
        Output:
            True - success
            False - failure
        Raises:
        """
        movie = self._repository.select_by_id(movie_id)
        if not movie:
            Session.set_message(self._repository.template_message_id_not_exists % movie_id)
            return False

        #if here all good
        new_movie = copy.deepcopy(movie)
        if movie_title:
            new_movie.title = movie_title
        if movie_type:
            new_movie.type = movie_type
        if movie_description:
            new_movie.description = movie_description

        self._repository.update(new_movie)

        return True

    def search(self, movie_id="", movie_title="", movie_type="", movie_description=""):
        """
        Search a client by all criteria
        Time complexity:
            O(n)
        """
        movies = self.get_all()
        found_movies = []

        # recursive search
        def recursive_search(movies, index=0):
            # base case
            if index == len(movies):
                return

            appended = False
            if movie_id and not appended:
                if movie_id == movies[index].id:
                    found_movies.append(movies[index])
                    appended = True

            if movie_title and not appended:
                if movie_title.lower() in movies[index].title.lower():
                    found_movies.append(movies[index])
                    appended = True

            if movie_type and not appended:
                if movie_type.lower() in movies[index].type.lower():
                    found_movies.append(movies[index])
                    appended = True

            if movie_description and not appended:
                if movie_description.lower() in movies[index].description.lower():
                    found_movies.append(movies[index])
                    appended = True

            recursive_search(movies, index + 1)

        recursive_search(movies, 0)
        # iterative search
        # appended = False  # append guard
        # for movie in movies:
        #     if movie_id and not appended:
        #         if movie_id == movie.id:
        #             found_movies.append(movie)
        #             appended = True
        #
        #     if movie_title and not appended:
        #         if movie_title.lower() in movie.title.lower():
        #             found_movies.append(movie)
        #             appended = True
        #
        #     if movie_type and not appended:
        #         if movie_type.lower() in movie.type.lower():
        #             found_movies.append(movie)
        #             appended = True
        #
        #     if movie_description and not appended:
        #         if movie_description.lower() in movie.description.lower():
        #             found_movies.append(movie)
        #             appended = True
        #
        #     appended = False

        return found_movies
