<?php
require_once 'config.php';
require_once 'include/Destination.php';
require_once 'include/Template.php';

$action = isset($_POST['action']) ? $_POST['action'] : null;

function exit_error($data)
{
    exit(json_encode(['status' => 'error', 'data' => $data]));
}

function exit_success($data, array $extra = [])
{
    exit(json_encode(['status' => 'success', 'data' => $data] + $extra));
}

if (!$action) exit_error('Action is not set');


switch ($action)
{
    case 'add':
        try
        {
            $id = Destination::add(
                $_POST['name'],
                $_POST['description'],
                $_POST['targets'],
                $_POST['country'],
                $_POST['cost-per-day']
            );
            exit_success(
                'Destination added',
                [
                    'html' => Template::getDestination(
                        $id,
                        $_POST['name'],
                        $_POST['description'],
                        $_POST['targets'],
                        $_POST['country'],
                        $_POST['cost-per-day']
                    )
                ]
            );
        }
        catch (Exception $e)
        {
            exit_error($e->getMessage());
        }

        break;

    case 'update':
        try
        {
            Destination::update(
                $_POST['id'],
                $_POST['name'],
                $_POST['description'],
                $_POST['targets'],
                $_POST['country'],
                $_POST['cost-per-day']
            );
            exit_success('Destination updated');
        }
        catch (Exception $e)
        {
            exit_error($e->getMessage());
        }
        break;

    case 'delete':
        try
        {
            Destination::delete($_POST['id']);
            exit_success('Destination deleted');
        }
        catch (Exception $e)
        {
            exit_error($e->getMessage());
        }

        break;

    default:
        exit_error('Action does not exist');
}