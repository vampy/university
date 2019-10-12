<?php
require_once 'DB.php';

class Destination
{
    /**
     * @return array|int|null
     * @throws DBException
     */
    public static function getAll()
    {
        return DB::get()->query(
            'SELECT D.*, C.name as `country_name`
            FROM destinations D JOIN countries C ON D.country_id = C.id',
            DB::FETCH_ALL
        );
    }

    public static function getAllByCountries()
    {
        $destinations = DB::get()->query(
            'SELECT D.*, C.name as `country_name`
            FROM destinations D JOIN countries C ON D.country_id = C.id ORDER BY D.country_id',
            DB::FETCH_ALL
        );
        $return = [];

        // group by country
        foreach ($destinations as $destination)
        {
            // country does not exist
            if (!isset($return[$destination['country_name']]))
            {
                $return[$destination['country_name']] = [];
            }

            $return[$destination['country_name']][] = $destination;
        }

        return $return;
    }


    /**
     * @param string $name
     *
     * @return int Id of the inserted country
     * @throws DBException
     */
    public static function addCountry($name)
    {
        DB::get()->query("INSERT INTO countries(name) VALUES(:name)", DB::NOTHING, [':name' => $name]);

        return (int)DB::get()->lastInsertId();
    }

    /**
     * @param string $name
     *
     * @return array|int|null
     * @throws DBException
     */
    public static function getCountryID($name)
    {
        return DB::get()->query(
            "SELECT id FROM countries WHERE LOWER(name) = :country_name",
            DB::FETCH_FIRST_COLUMN,
            [':country_name' => strtolower($name)]
        );
    }

    /**
     * @param int $id
     *
     * @return bool
     * @throws DBException
     */
    public static function exists($id)
    {
        return (bool)DB::get()->query(
            "SELECT COUNT(*) FROM destinations WHERE id = :id",
            DB::FETCH_FIRST_COLUMN,
            [':id' => $id],
            [':id' => DB::PARAM_INT]
        );
    }

    /**
     * @param int $id
     *
     * @throws DBException
     * @throws Exception
     */
    public static function delete($id)
    {
        $affected = DB::get()->query(
            "DELETE FROM destinations WHERE id = :id",
            DB::ROW_COUNT,
            [':id' => $id],
            [':id' => DB::PARAM_INT]
        );

        if (!$affected)
        {
            throw new Exception('The destination does not exist');
        }
    }

    /**
     * @param string $name
     * @param string $description
     * @param string $targets
     * @param string $country_name
     * @param int    $cost_per_day
     *
     * @return int Id of the destination
     */
    public static function add($name, $description, $targets, $country_name, $cost_per_day)
    {
        $country_id = static::getCountryID($country_name);

        // add country
        if (!$country_id)
        {
            $country_id = static::addCountry($country_name);
        }

        DB::get()->query(
            "INSERT INTO destinations(name, description, targets, country_id, cost_per_day)
                              VALUES(:name, :description, :targets, :country_id, :cost_per_day)",
            DB::NOTHING,
            [
                ':name'         => $name,
                ':description'  => $description,
                ':targets'      => $targets,
                ':country_id'   => $country_id,
                ':cost_per_day' => $cost_per_day
            ],
            [
                ':country_id'   => DB::PARAM_INT,
                ':cost_per_day' => DB::PARAM_INT
            ]
        );

        return DB::get()->lastInsertId();
    }

    /**
     * @param int    $id
     * @param string $name
     * @param string $description
     * @param string $targets
     * @param string $country_name
     * @param int    $cost_per_day
     *
     * @throws DBException
     * @throws Exception
     */
    public static function update($id, $name, $description, $targets, $country_name, $cost_per_day)
    {
        if (!$id || !$name || !$description || !$targets || !$country_name || !$cost_per_day)
        {
            throw new Exception('All fields must be not empty');
        }

        $country_id = static::getCountryID($country_name);

        // add country
        if (!$country_id)
        {
            $country_id = static::addCountry($country_name);
        }

        DB::get()->query(
            "UPDATE destinations
            SET name = :name, description = :description, targets = :targets, country_id = :country_id,
                cost_per_day = :cost_per_day
            WHERE id = :id",
            DB::NOTHING,
            [
                ':id'           => $id,
                ':name'         => $name,
                ':description'  => $description,
                ':targets'      => $targets,
                ':country_id'   => $country_id,
                ':cost_per_day' => $cost_per_day
            ],
            [
                ':id'           => DB::PARAM_INT,
                ':country_id'   => DB::PARAM_INT,
                ':cost_per_day' => DB::PARAM_INT
            ]
        );
    }
}
