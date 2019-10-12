<?php

class DBException extends Exception
{
}

define('DEBUG_MODE', true);
if (!defined('DEBUG_MODE')) define('DEBUG_MODE', false);

/**
 * Class DB, handles all the database connections
 */
class DB
{
    /**
     * The PDO database handle
     * @var PDO
     */
    private $connection;

    /**
     * Flag to see if we currently in a sql transaction
     * @var bool
     */
    private $in_transaction = false;

    /**
     * The singleton used for the database connection
     * @var DB
     */
    private static $instance;

    // Fake enumeration
    const ROW_COUNT = 1;
    const FETCH_ALL = 2;
    const FETCH_FIRST = 3;
    const FETCH_FIRST_COLUMN = 4;
    const NOTHING = 99;

    // Alias for PDO Constants
    const PARAM_BOOL = PDO::PARAM_BOOL;
    const PARAM_INT = PDO::PARAM_INT;
    const PARAM_NULL = PDO::PARAM_NULL;
    const PARAM_STR = PDO::PARAM_STR;


    /**
     * @param string $host
     * @param string $name
     * @param string $username
     * @param string $password
     */
    private function __construct($host, $name, $username, $password)
    {
        try
        {
            $this->connection =
                new PDO(sprintf("mysql:host=%s;dbname=%s;charset=utf8mb4", $host, $name), $username, $password);
            if (!$this->connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION))
            {
                throw new Exception("setAttribute ATTR_ERRMODE failed");
            }
            if (!$this->connection->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC))
            {
                throw new Exception("setAttribute ATTR_DEFAULT_FETCH_MODE failed");
            }
        }
        catch (Exception $e)
        {
            exit("ERROR: Can not connect to the database. " . $e->getMessage());
        }
    }


    /**
     * @return array
     */
    private static function getReturnTypes()
    {
        return [
            static::ROW_COUNT,
            static::FETCH_ALL,
            static::FETCH_FIRST_COLUMN,
            static::FETCH_FIRST,
            static::NOTHING
        ];
    }

    /**
     * Get the DBConnection singleton
     *
     * @return \DB
     */
    public static function get()
    {
        if (!static::$instance)
        {
            static::$instance = new static('localhost', 'school_web_exam', 'root', '');
        }

        return static::$instance;
    }

    /**
     * Get the internal PDO connection object
     * @return PDO
     */
    public function getConnection()
    {
        return $this->connection;
    }

    /**
     * Perform a query on the database
     *
     * @param string $query          The sql string
     * @param int    $return_type    The type of return. Use the class constants
     * @param array  $prepared_pairs An associative array having mapping between variables for prepared statements and
     *                               values
     * @param array  $data_types     variables in prepared statement for which data type should be explicitly mentioned
     *
     * @throws DBException
     *
     * @return array|int|null depending of the return type
     */
    public function query(
        $query,
        $return_type = DB::NOTHING,
        array $prepared_pairs = [],
        array $data_types = []
    ) {
        if (DEBUG_MODE && !in_array($return_type, static::getReturnTypes()))
        {
            trigger_error("Return type is invalid");
            exit;
        }
        try
        {
            $sth = $this->connection->prepare($query);
            foreach ($prepared_pairs as $key => $param)
            {
                if (isset($data_types[$key]))
                {
                    $sth->bindValue($key, $param, $data_types[$key]);
                }
                else
                {
                    $sth->bindValue($key, $param);
                }
            }
            $sth->execute();
            if ($return_type === static::NOTHING)
            {
                return null;
            }
            if ($return_type === static::ROW_COUNT)
            {
                return $sth->rowCount();
            }
            if ($return_type === static::FETCH_ALL)
            {
                return $sth->fetchAll(PDO::FETCH_ASSOC);
            }
            if ($return_type === static::FETCH_FIRST)
            {
                return $sth->fetch(PDO::FETCH_ASSOC);
            }
            if ($return_type === static::FETCH_FIRST_COLUMN)
            {
                return $sth->fetchColumn();
            }
        }
        catch (PDOException $e)
        {
            if ($this->in_transaction)
            {
                $success = $this->connection->rollback();
                if (DEBUG_MODE && !$success)
                {
                    trigger_error("A PDO exception occurred during during a transaction, but the rollback failed");
                }
            }
            if (DEBUG_MODE)
            {
                trigger_error("Database Error");
                var_dump($e->errorInfo);
                printf(
                    "SQLSTATE ERR: %s<br>\nmySQL ERR: %s<br>\nMessage: %s<br>\nQuery: %s<br>",
                    $e->errorInfo[0],
                    $e->errorInfo[1],
                    isset($e->errorInfo[2]) ? $e->errorInfo[2] : "",
                    $query
                );
                echo "Fields data: <br>";
                var_dump($prepared_pairs);
            }
            throw new DBException($e->errorInfo[0]);
        }
        throw new DBException("Unexpected reach of end of query(). Possibly return_type was invalid");
    }

    /**
     * Get the last id inserted into the database
     *
     * @return string
     */
    public function lastInsertId()
    {
        return $this->connection->lastInsertId();
    }
}