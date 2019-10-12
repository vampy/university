<?php
require_once 'include/Destination.php';
require_once 'include/Template.php';
?>
<h2>All destination grouped by country</h2>
<section class="section-view">
    <?php
    $countries = Destination::getAllByCountries();
    foreach ($countries as $name => $destinations)
    {
        $return = sprintf('<h3>%s</h3>', $name);
        foreach ($destinations as $destination)
        {
            $return .= Template::build_input('Name', 'name', $destination['name']);
            $return .= sprintf(
                "<p><label>Description: <textarea name='description'>%s</textarea></label> </p>",
                $destination['description']
            );
            $return .= Template::build_input('Targets', 'targets', $destination['targets']);
            $return .= Template::build_input('Country', 'country', $destination['country_name']);
            $return .= Template::build_input('Cost/day', 'cost-per-day', $destination['cost_per_day']);
            $return .= '<hr>';
        }
        echo $return;
    }
    ?>
</section>
