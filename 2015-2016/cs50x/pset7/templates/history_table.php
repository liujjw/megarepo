<table class="table table-striped" style="font-size:110%; text-align:left;">
	<thead>
		<tr>
			<th>ID</th>
			<th>TRANSACTION</th>
			<th>STOCK</th>
			<th>SHARES</th>
			<th>DATE/TIME</th>
			<th>PRICE</th>
		</tr>
	</thead>

<?php if(isset($data)): ?>
	<tbody>
		<?php foreach($data as $transaction): ?>
			<tr>
				<td><?= $transaction["transaction_id"] ?></td>
				<td><?= $transaction["transaction"] ?></td>
				<td><?= $transaction["stock"] ?></td>
				<td><?= $transaction["shares"] ?></td>
				<td><?= $transaction["time"] ?></td>
				<td><?= number_format($transaction["price"],2,".",",") ?></td>
			</tr>
		<?php endforeach ?>
	</tbody>
<?php endif ?>
</table>

<div>
	<a href="index.php">Home</a>
</div>