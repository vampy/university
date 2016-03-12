#!/usr/bin/python
import copy

from controller import Controller
from domain.client import Client
from domain.session import Session
from repository.repository import RepositoryException


class ClientController(Controller):
    def store(self, client_id, client_name, client_cnp):
        """
        Store a new client
        Input:
            instance - an object
        Output:
            True - success
            False - failure
        Raises:
        """
        client_obj = Client(client_id=client_id, client_name=client_name, client_cnp=client_cnp)
        try:
            self._repository.insert(client_obj)
        except RepositoryException as e:
            Session.set_message(e.message)
            return False

        return True

    def update(self, client_id, client_name, client_cnp):
        """
        Update a client by id
        Time complexity:
            O(1)
        Input:
            instance - an object
        Output:
            True - success
            False - failure
        Raises:
        """
        client = self._repository.select_by_id(client_id)
        if not client:
            Session.set_message(self._repository.template_message_id_not_exists % client_id)
            return False

        # if here all good
        new_client = copy.deepcopy(client)
        if client_name:
            new_client.name = client_name
        if client_cnp:
            new_client.cnp = client_cnp

        # reference old object to new
        self._repository.update(new_client)

        return True

    def search(self, client_id="", client_name="", client_cnp=""):
        """
        Search a client by all criteria
        Time complexity:
            O(n)
        """
        clients = self.get_all()
        found_clients = []

        # recursive search
        def recursive_search(clients, index=0):
            # base case
            if index == len(clients):
                return

            appended = False
            if client_id and not appended:
                if client_id == clients[index].id:
                    found_clients.append(clients[index])
                    appended = True

            if client_name and not appended:
                if client_name.lower() in clients[index].name.lower():
                    found_clients.append(clients[index])
                    appended = True

            if client_cnp and not appended:
                if client_cnp == clients[index].cnp:
                    found_clients.append(clients[index])
                    appended = True

            recursive_search(clients, index + 1)

        recursive_search(clients, 0)

        # iterative search
        # appended = False  # append guard
        # for client in clients:
        #     if client_id and not appended:
        #         if client_id == client.id:
        #             found_clients.append(client)
        #             appended = True
        #
        #     if client_name and not appended:
        #         if client_name.lower() in client.name.lower():
        #             found_clients.append(client)
        #             appended = True
        #
        #     if client_cnp and not appended:
        #         if client_cnp == client.cnp:
        #             found_clients.append(client)
        #             appended = True
        #
        #     appended = False

        return found_clients
