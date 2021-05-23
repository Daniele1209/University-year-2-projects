use DBMS_hw
go

set deadlock_priority high

-- second query
begin tran
	update astronaut set age='###' where id=1
	waitfor delay '00:00:10'
	update astronaut set age='###' where id=2
	exec log_procedure 'deadlock update', null, null 
commit tran
