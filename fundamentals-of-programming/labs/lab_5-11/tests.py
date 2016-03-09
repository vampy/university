#!/usr/bin/python

import unittest

import utils.number

from domain.client import Client
from domain.movie import Movie
from repository.client import ClientRepository, RepositoryException
from repository.movie import MovieRepository


class TestConvert(unittest.TestCase):
    def setUp(self):
        pass

    def test_convert_to_int(self):
        self.assertEqual(utils.number.convert_to_int("23"), 23)
        self.assertEqual(utils.number.convert_to_int("0"), 0)
        self.assertEqual(utils.number.convert_to_int("-23"), -23)
        self.assertEqual(utils.number.convert_to_int(245), 245)
        self.assertEqual(utils.number.convert_to_int(-245), -245)
        self.assertEqual(utils.number.convert_to_int(0), 0)
        self.assertIsNone(utils.number.convert_to_int("43.53"))
        self.assertIsNone(utils.number.convert_to_int("25sn"))
        self.assertIsNone(utils.number.convert_to_int("s45"))
        self.assertIsNone(utils.number.convert_to_int("string"))

    def test_convert_to_float(self):
        self.assertEqual(utils.number.convert_to_float("23.45"), 23.45)
        self.assertEqual(utils.number.convert_to_float("0"), 0.0)
        self.assertEqual(utils.number.convert_to_float("-0"), 0.0)
        self.assertEqual(utils.number.convert_to_float("-23"), -23.0)
        self.assertEqual(utils.number.convert_to_float(245), 245.0)
        self.assertEqual(utils.number.convert_to_float(-245), -245.0)
        self.assertEqual(utils.number.convert_to_float(-245.8457), -245.8457)
        self.assertEqual(utils.number.convert_to_float(0), 0.0)
        self.assertEqual(utils.number.convert_to_float("43.53"), 43.53)
        self.assertIsNone(utils.number.convert_to_float("25sn"))
        self.assertIsNone(utils.number.convert_to_float("s45"))
        self.assertIsNone(utils.number.convert_to_float("string"))


class TestMovie(unittest.TestCase):
    def setUp(self):
        self.movie_id = 1
        self.movie_title = "Avatar"
        self.movie_type = "action sci-fi"
        self.movie_description = "desc"
        self.movie = Movie(self.movie_id, self.movie_title, self.movie_type, self.movie_description)

    def test_get(self):
        self.assertEqual(self.movie.id, self.movie_id)
        self.assertEqual(self.movie.title, self.movie_title)
        self.assertEqual(self.movie.description, self.movie_description)
        self.assertEqual(self.movie.type, self.movie_type)
        self.assertNotEqual(self.movie.id, 999)

    def test_set(self):
        self.movie.title = "Avatar 2009"
        self.movie.description = "An awesome movie"
        self.movie.id = 99
        self.movie.type = "action"

        self.assertEqual(self.movie.id, 99)
        self.assertEqual(self.movie.title, "Avatar 2009")
        self.assertEqual(self.movie.description, "An awesome movie")
        self.assertEqual(self.movie.type, "action")

        self.movie.id = self.movie_id
        self.movie.title = self.movie_title
        self.movie.type = self.movie_type
        self.movie.description = self.movie_description
        self.assertEqual(self.movie.id, self.movie_id)
        self.assertEqual(self.movie.title, self.movie_title)
        self.assertEqual(self.movie.description, self.movie_description)
        self.assertEqual(self.movie.type, self.movie_type)

        self.movie.title = "Avatar 20091"
        self.movie.description = "An awesome movie"
        self.movie.id = 99
        self.movie.type = "action"

        self.assertEqual(self.movie.id, 99)
        self.assertEqual(self.movie.title, "Avatar 20091")
        self.assertEqual(self.movie.description, "An awesome movie")
        self.assertEqual(self.movie.type, "action")

        self.movie.id = self.movie_id
        self.movie.title = self.movie_title
        self.movie.type = self.movie_type
        self.movie.description = self.movie_description
        self.assertEqual(self.movie.id, self.movie_id)
        self.assertEqual(self.movie.title, self.movie_title)
        self.assertEqual(self.movie.description, self.movie_description)
        self.assertEqual(self.movie.type, self.movie_type)

    def test_del(self):
        del self.movie.description
        try:
            self.movie.description
            self.assertFalse(False)
        except Exception:
            self.assertTrue(True)

        self.movie.id = self.movie_id
        self.movie.title = self.movie_title
        self.movie.type = self.movie_type
        self.movie.description = self.movie_description
        self.assertEqual(self.movie.id, self.movie_id)
        self.assertEqual(self.movie.title, self.movie_title)
        self.assertEqual(self.movie.description, self.movie_description)
        self.assertEqual(self.movie.type, self.movie_type)


class TestClient(unittest.TestCase):
    def setUp(self):
        self.client_id = 1
        self.client_name = "Daniel"
        self.client_cnp = "1923425252"
        self.client = Client(self.client_id, self.client_name, self.client_cnp)

    def test_get(self):
        self.assertEqual(self.client.id, self.client_id)
        self.assertEqual(self.client.name, self.client_name)
        self.assertEqual(self.client.cnp, self.client_cnp)
        self.assertNotEqual(self.client.id, 999)
        self.assertEqual(str(self.client), "%s\t\t\t"*3 %(self.client_id, self.client_name, self.client_cnp))

    def test_set(self):
        self.client.name = "Bruce"
        self.client.cnp = "99999999999"

        self.assertEqual(self.client.name, "Bruce")
        self.assertEqual(self.client.cnp, "99999999999")

        self.client.id = self.client_id
        self.client.name = self.client_name
        self.client.cnp = self.client_cnp
        self.assertEqual(self.client.id, self.client_id)
        self.assertEqual(self.client.name, self.client_name)
        self.assertEqual(self.client.cnp, self.client_cnp)

        self.client.name = "Bruce"
        self.client.cnp = "99999999999"

        self.assertEqual(self.client.name, "Bruce")
        self.assertEqual(self.client.cnp, "99999999999")

        self.client.id = self.client_id
        self.client.name = self.client_name
        self.client.cnp = self.client_cnp
        self.assertEqual(self.client.id, self.client_id)
        self.assertEqual(self.client.name, self.client_name)
        self.assertEqual(self.client.cnp, self.client_cnp)

    def test_del(self):
        del self.client.cnp
        try:
            self.client.cnp
            self.assertFalse(False)
        except Exception:
            self.assertTrue(True)

        self.client.id = self.client_id
        self.client.name = self.client_name
        self.client.cnp = self.client_cnp
        self.assertEqual(self.client.id, self.client_id)
        self.assertEqual(self.client.name, self.client_name)
        self.assertEqual(self.client.cnp, self.client_cnp)


class TestFileClientRepository(unittest.TestCase):
    def setUp(self):
        self.repo = ClientRepository()
        self.client_id = 1
        self.client_name = "Daniel"
        self.client_cnp = "1923425252"

    def test_init(self):
        self.assertIsNotNone(Client(self.client_id, self.client_name, self.client_cnp))

    def test_insert(self):
        self.assertTrue(self.repo.insert(Client(self.client_id, self.client_name, self.client_cnp)))
        self.assertRaises(RepositoryException, self.repo.insert, Client(self.client_id, self.client_name, self.client_cnp))

    def test_get(self):
        self.assertEqual(self.client_id, 1)
        self.assertEqual(self.client_name, "Daniel")
        self.assertEqual(self.client_cnp, "1923425252")


class TestClientRepository(unittest.TestCase):
    def setUp(self):
        self.repo = ClientRepository()
        self.client_id = 1
        self.client_name = "Daniel"
        self.client_cnp = "1923425252"

    def test_init(self):
        self.assertIsNotNone(Client(self.client_id, self.client_name, self.client_cnp))

    def test_insert(self):
        self.assertTrue(self.repo.insert(Client(self.client_id, self.client_name, self.client_cnp)))
        self.assertRaises(RepositoryException, self.repo.insert, Client(self.client_id, self.client_name, self.client_cnp))
        self.assertTrue(self.repo.delete_by_id(1))


    def test_del(self):
        self.assertRaises(RepositoryException, self.repo.delete_by_id, 1)

    def test_get(self):
        self.assertEqual(self.client_id, 1)
        self.assertEqual(self.client_name, "Daniel")
        self.assertEqual(self.client_cnp, "1923425252")


class TestMovieRepository(unittest.TestCase):
    def setUp(self):
        self.repo = MovieRepository()
        self.movie_id = 1
        self.movie_title = "Avatar"
        self.movie_type = "action sci-fi"
        self.movie_description = "desc"

    def test_init(self):
        self.assertIsNotNone(Movie(self.movie_id, self.movie_title, self.movie_type, self.movie_description))

    def test_insert(self):
        self.assertTrue(self.repo.insert(Movie(self.movie_id, self.movie_title, self.movie_type)))
        self.assertRaises(RepositoryException, self.repo.insert, Movie(self.movie_id, self.movie_title, self.movie_type))
        self.assertTrue(self.repo.delete_by_id(1))


    def test_del(self):
        self.assertRaises(RepositoryException, self.repo.delete_by_id, 1)

    def test_get(self):
        self.assertEqual(self.movie_id, 1)
        self.assertEqual(self.movie_description, "desc")
        self.assertEqual(self.movie_title, "Avatar")
        self.assertEqual(self.movie_type, "action sci-fi")


class TestFileMovieRepository(unittest.TestCase):
    def setUp(self):
        self.repo = MovieRepository()
        self.movie_id = 1
        self.movie_title = "Avatar"
        self.movie_type = "action sci-fi"
        self.movie_description = "desc"

    def test_init(self):
        self.assertIsNotNone(Movie(self.movie_id, self.movie_title, self.movie_type, self.movie_description))

    def test_insert(self):
        self.assertTrue(self.repo.insert(Movie(self.movie_id, self.movie_title, self.movie_type)))
        self.assertRaises(RepositoryException, self.repo.insert, Movie(self.movie_id, self.movie_title, self.movie_type))

    def test_get(self):
        self.assertEqual(self.movie_id, 1)
        self.assertEqual(self.movie_description, "desc")
        self.assertEqual(self.movie_title, "Avatar")
        self.assertEqual(self.movie_type, "action sci-fi")


if __name__ == "__main__":
    unittest.main()