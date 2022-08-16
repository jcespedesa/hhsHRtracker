function confirmDelete()
{
	var agree=confirm("Are you sure you want to delete this record?");
	if(agree)
		return true;
					
	else
		return false;
     				
}

function protectedItem()
{
	alert("This item is protected and cannot be deleted...");
	
	return true;
	
}

function confirmDeletePic()
{
	var agree=confirm("Are you sure you want to delete this picture?");
	if(agree)
		return true;
					
	else
		return false;
     				
}