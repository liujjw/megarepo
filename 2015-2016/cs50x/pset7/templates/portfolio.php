<div>
    
    <!-- <img alt="Under Construction" src="/img/construction.gif"/> -->

    <div id="portfolio_username" style="font-size:320%;">
    	<div style="color:#216C2A;"><?= $username ?></div>
    </div>

    <div id="portfolio_money" style="font-size:110%; padding-bottom:39px;">
		<div style="color:#85bb65;">Balance: $<?= number_format($money, 2) ?></div>
	</div>

	<table class="table table-striped" style="font-size:110%; text-align:left;">
		<thead> 
			<tr> 
				<th>NAME</th> 
				<th>SYMBOL</th> 
				<th>PRICE</th> 
				<th>SHARES</th>
				<th>WORTH</th>
    		</tr>
		</thead>	  

	<?php if(isset($portfolio)): ?>    
	    <?php foreach($portfolio as $row): ?>
	    	<tbody>
	    		<tr>
		    	    <td> <?= $row["name"]?> </td>
		    		<td> <?= $row["symbol"]?> </td>
		    		<td> <?= $row["prices"]?> </td>
		    		<td> <?= $row["shares"]?> </td>
		    		<td> $<?= $row["cost"]?> </td>
		    	</tr>
	    	</tbody>
		<?php endforeach ?>
	<?php endif ?>
	</table>

</div>

<div id="logout">
    <a href="logout.php">Log Out</a>
	<div style="padding-top:20px">
	    <!--change password, just call register again?-->
	    <a href="register.php">Change Password</a>
    </div>
</div>
