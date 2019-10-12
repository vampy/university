<?php

class Template
{
    public static function echo_input($label, $input_name, $value)
    {
        echo static::build_input($label, $input_name, $value);
    }

    public static function build_input($label, $input_name, $value)
    {
        return sprintf(
            "<p><label>%s: <input type='text' value='%s' name='%s' required></label></p>",
            $label,
            $value,
            $input_name
        );
    }

    public static function getDestination($id, $name, $description, $targets, $country_name, $cost_per_day)
    {
        $return = '<form>';
        $return .= sprintf('<h3>%s</h3>', $name);
        $return .= '<p class="status"><p>';

        $return .= static::build_input('Name', 'name', $name);
        $return .= sprintf(
            "<p><label>Description: <textarea name='description'>%s</textarea></label> </p>",
            $description
        );
        $return .= static::build_input('Targets', 'targets', $targets);
        $return .= static::build_input('Country', 'country', $country_name);
        $return .= static::build_input('Cost/day', 'cost-per-day', $cost_per_day);
        $return .= sprintf("<input type='hidden' value='%d' name='id'>", $id);
        $return .= '<button type="button" class="destination-update">Update</button><button type="button" class="destination-delete">Delete</button>';
        $return .= '</form><hr>';

        return $return;
    }
}