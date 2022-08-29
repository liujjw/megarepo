<!DOCTYPE html>

<html>

    <head>

        <link href="/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="/css/bootstrap-theme.min.css" rel="stylesheet"/>
        <link href="/css/styles.css" rel="stylesheet"/>

        <?php if (isset($title)): ?>
            <title>C$50 Finance: <?= htmlspecialchars($title) ?></title>
        <?php else: ?>
            <title>C$50 Finance</title>
        <?php endif ?>

        <script src="/js/jquery-1.11.1.min.js"></script>
        <script src="/js/bootstrap.min.js"></script>
        <script src="/js/scripts.js"></script>

    </head>

    <body>

        <div class="container">

            <div id="top">
                <a href="/"><img alt="C$50 Finance" src="/img/logo.png" width="800" height="80"/></a>
            </div>

            <div id="middle">
                <?php if($_SERVER['PHP_SELF'] != '/login.php' && $_SERVER['PHP_SELF'] != '/register.php'): ?>
                    <div style="display: inline-block; font-size:130%;">
                        <ul class="nav nav-pills">
                            <li><a href="index.php">PORTFOLIO</a></li>
                            <li><a href="quote.php">QUOTE</a></li>
                            <li><a href="buy.php">BUY</a></li>
                            <li><a href="sell.php">SELL</a></li>
                            <li><a href="history.php">HISTORY</a></li>
                        </ul>
                    </div>
                    <?php endif ?>