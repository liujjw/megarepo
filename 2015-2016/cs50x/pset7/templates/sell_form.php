<form action="sell.php" method="post">
	<fieldset>
		<div class="form-group">
		<select class="form-control" name="symbols">
			<option disabled selected value="">Symbol</option>
			<?php 
				foreach($stocks as $one_stock)
				{
					echo "<option value='{$one_stock['symbol']}'>{$one_stock['symbol']}</option>";
				}
			?>
		</select>
		</div>
		<div class="form-group">
			<button class="btn btn-danger" type="submit">Sell All</button>
		</div>
	</fieldset>		
</form>

<div style="padding-top:10px;">
    <a href="index.php">Home</a>
</div>