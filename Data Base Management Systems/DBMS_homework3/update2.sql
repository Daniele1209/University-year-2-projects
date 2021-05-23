use DBMS_hw
go

-- second query
set transaction isolation level snapshot

begin tran
	declare @name varchar(50) 
	select * from astronaut where id=1
	waitfor delay '00:00:10'
	select * from astronaut where id=2
	update astronaut set age='###', @name=name where id=1
	exec log_procedure 'update conflict update 2', @name, null 
commit tran
