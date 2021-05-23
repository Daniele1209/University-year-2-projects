create table LogTable(
Lid int identity primary key,
TypeOperation varchar(50),
TableOperation varchar(50),
ExecutionDate datetime)gocreate OR alter procedure log_procedure
	@operation_type VARCHAR(100),
	@operation_table VARCHAR(100),
	@time VARCHAR(100)
as
begin
	insert into LogTable (TypeOperation, TableOperation, ExecutionDate) VALUES
	(@operation_type, @operation_table, getdate())
end
go

select * from LogTable