<?php
	$s=socket_create(AF_INET, SOCK_DGRAM, 0);
	socket_bind($s, '0.0.0.0', 8888);
	socket_recvfrom($s, $message, 1204, 0, $client_ip, $client_port);
	$count=strlen($message);

	socket_sendto($s, $count, 1, 0, $client_ip, $client_port);
?>
