(function() {
    // querySelector, jQuery style
    var $ = function(selector) {
        return document.querySelector(selector);
    };

    function request(method, data, url, callback, modifyRequestCallback) {
        modifyRequestCallback = modifyRequestCallback || function(_) {};
        var httpRequest = new XMLHttpRequest();

        if (!httpRequest) {
            console.error('Giving up :( Cannot create an XMLHTTP instance');
            return false;
        }

        httpRequest.onload = function() {
            try {
                if (this.status === 200) {
                    callback(this);
                } else {
                    console.error('There was a problem with the request.');
                }
            } catch (e) {
                console.error('Caught Exception: ' + e);
            }
        };
        httpRequest.open(method, url, true);
        modifyRequestCallback(httpRequest);
        httpRequest.send(data);
    }

    function getRequest(url, callback) {
        return request('GET', null, url, callback);
    }

    function postRequest(data, url, callback) {
        return request('POST', data, url, callback);
    }

    function setStatus(element, jsonData) {
        if (jsonData['status'] === 'error') {
            element.classList.remove('text-success');
            element.classList.add('text-error');
            element.innerHTML = jsonData['data'];
        } else if (jsonData['status'] === 'success') {
            element.classList.remove('text-error');
            element.classList.add('text-success');
            element.innerHTML = jsonData['data'];
        } else {
            console.error('setStatus', jsonData);
        }
    }

    // Delete destination
    var sectionEdit = $('.section-edit');
    var initEventsDelete = function() {
        var deleteButtons = sectionEdit.querySelectorAll('.destination-delete');
        for (var i = 0; i < deleteButtons.length; i++) {
            deleteButtons[i].addEventListener('click', function() {
                // Assume the form is the parent element
                var form = this.parentElement;
                var status = form.querySelector('.status');

                var data = new FormData(form);
                data.append('action', 'delete');

                postRequest(data, 'ajax.php', function(request) {
                    var jsonData = JSON.parse(request.responseText);
                    //setStatus(status, jsonData);

                    // remove from DOM
                    if (jsonData['status'] === 'success') {
                        form.remove();
                    }
                });
            });
        }
    };

    // Update destination
    var initEventsUpdate = function() {
        var updateButtons = sectionEdit.querySelectorAll('.destination-update');
        for (var i = 0; i < updateButtons.length; i++) {
            updateButtons[i].addEventListener('click', function() {
                // Assume the form is the parent element
                var form = this.parentElement;
                var status = form.querySelector('.status');

                var data = new FormData(form);
                data.append('action', 'update');

                postRequest(data, 'ajax.php', function(request) {
                    var jsonData = JSON.parse(request.responseText);
                    setStatus(status, jsonData);
                });
            });
        }
    };

    // Add new destination
    var formAdd = $('.section-add form');
    var formAddStatus = formAdd.querySelector('.status');
    formAdd.addEventListener('submit', function(event) {
        event.preventDefault();

        var data = new FormData(this);
        data.append('action', 'add');

        postRequest(data, 'ajax.php', function(request) {
            var jsonData = JSON.parse(request.responseText);
            setStatus(formAddStatus, jsonData);

            // Reset form and append extra stuff
            if (jsonData['status'] === 'success') {
                formAdd.reset();
                sectionEdit.innerHTML = jsonData['html'] + sectionEdit.innerHTML;
                initEventsDelete();
                initEventsUpdate();
            }
        });
    });

    initEventsDelete();
    initEventsUpdate();

    // define menu listeners
    var content = $('.content');
    $('.menu-all').addEventListener('click', function() {
        getRequest('destinations-all.php', function(request) {
            content.innerHTML = request.responseText;
            initEventsDelete();
            initEventsUpdate();
        });
    });

    $('.menu-country').addEventListener('click', function() {
        getRequest('destinations-country.php', function(request) {
            content.innerHTML = request.responseText;
        });
    });

})();
