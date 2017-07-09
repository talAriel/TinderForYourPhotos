<?php

switch($_SERVER['REQUEST_METHOD'])
        {
			case 'GET': $the_request = $_GET['goto']; break;
			case 'POST': $the_request = $_POST['goto']; break;
			default:
        }

switch($the_request)
        {
			case 'updateRecord': 
				$url = $_GET["url"];
				$liked = $_GET["liked"];
				$command = escapeshellcmd('python ' . $the_request . '.py ' . $url . ' ' . $liked);
				break;
			case 'deletePhoto':
			case 'undoDelete':
				$url = $_GET["url"];
				$command = escapeshellcmd('python ' . $the_request . '.py ' . $url);
				break;
			case 'getNext':
			default:        
				$command = escapeshellcmd('python ' . $the_request . '.py');
				break;
        }
$output = shell_exec($command);
echo $output;
?>

