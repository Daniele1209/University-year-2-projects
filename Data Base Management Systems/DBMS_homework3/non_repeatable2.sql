use DBMS_hw
go

set transaction isolation level repeatable read
--set transaction isolation level read committed

-- second query
begin tran
	select * from astronaut
	exec log_procedure 'non repeatable reads select', null, null
	waitfor delay '00:00:05'
	select * from astronaut
commit tran

