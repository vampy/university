<?php
require_once 'include/Destination.php';
require_once 'include/Template.php';
?>
<h2>All destination</h2>
<section class="section-edit">
    <?php
    $destinations = Destination::getAll();
    foreach ($destinations as $destination)
    {
        echo Template::getDestination(
            $destination['id'],
            $destination['name'],
            $destination['description'],
            $destination['targets'],
            $destination['country_name'],
            $destination['cost_per_day']
        );
    }
    ?>
</section>
