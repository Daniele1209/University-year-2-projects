<?php
	$port=8888;
	$message=rtrim(fgets(STDIN));
	$len=strlen($message);

	$s=socket_create(AF_INET, SOCK_DGRAM, 0);
	socket_sendto($s, $message, $len, 0, "127.0.0.1", $port);
	socket_recvfrom($s, $string, 10, 0, $serv_ip, $serv_port);

	echo $string.PHP_EOL;
?>
