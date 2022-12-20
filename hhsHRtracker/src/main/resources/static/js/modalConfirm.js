function confirmAssignation()
{
	var agree=confirm("Are you sure you want to automatic assign training to this employee based in his/her title?");
	if(agree)
		return true;
					
	else
		return false;
     				
}