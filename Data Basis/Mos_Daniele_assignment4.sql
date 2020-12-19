use Video_Games;
go

-- add the table data fro the 3 required tables
SET IDENTITY_INSERT Consoles ON
insert into Consoles(Cid, Model, Origin, Release, Price) values (3, 'Playstation 5', 'Japan', getdate(), 500)
SET IDENTITY_INSERT Consoles OFF
-- turn table into a multicolumn primary key table
alter table ConsoleDeveloper add constraint CD_Name_PK primary key (Cdid,Name)
SET IDENTITY_INSERT ConsoleDeveloper ON
insert into ConsoleDeveloper(Cdid, Name, Founding, Origin, CEO) values (3, 'Sony', getdate(), 'Japan', 'Howard Stringer')
SET IDENTITY_INSERT ConsoleDeveloper OFF
SET IDENTITY_INSERT Product ON
insert into Product(Prid, Consoles_id, Console_dev_id) values (1, 3, 3)
SET IDENTITY_INSERT Product OFF

go

--clean the test tables
delete from TestTables
delete from TestRuns
delete from TestRunTables
delete from TestViews
delete from TestRunViews
go

--insert into tables
insert into Tables(Name) values ('Consoles'), ('ConsoleDeveloper'), ('Product')
go

-- view with SELECT
create view View1 as
	select Pid, Name from Publisher
go 

create view View2 as
	select S.Smid, Sh.Name, S.Name
	from ShopManager S
	inner join Shop Sh on Sh.Sid = S.Shop_id
go

create view View3 as
	select Developer = D.Name, Consoles = C.Model
	from ConsoleDeveloper D
	inner join Product P on P.Console_dev_id = D.Cdid
	inner join Consoles C on P.Consoles_id = C.Cid
	group by D.Name, C.Model
go

--inserting the views
insert into Views values ('View1'), ('View2'), ('View3')
go


create or alter procedure deleteAll @table_name varchar(50) as
	declare @query varchar(100)
	set @query = 'Delete from ' + @table_name
	exec(@query)
go

-- inserting data
create or alter procedure insert_data @nb_rows int, @table_name varchar(50) as
	if @nb_rows < 1
		raiserror('Number or rows not valid !', 16, 1);

	declare @i int
	declare @Name varchar(50)
	declare @Name_m varchar(50)
	declare @City varchar(50)
	declare @City2 varchar(50)
	declare @Clients varchar(50)
	declare @S_id int
	declare @C_id int
	declare @Date date
	declare @Date2 date
	declare @Person varchar(50)

	set @i = 0
	while @i < @nb_rows
	begin
		if @table_name = 'ShopManager'
		begin
			set @Name = 'Shop ' + convert(varchar(10), @i)
			set @City = 'City ' + convert(varchar(10), @i)
			set @Clients = 1000 * @i
			insert into Shop(Name, City, Clients) values (@Name, @City, @Clients)
			select @S_id = Sid from Shop where Clients = @Clients
			set @Name_m = 'Manager ' + convert(varchar(10), @i)
			insert into ShopManager(Name, Shop_id) values (@Name_m, @S_id)
		end
		if @table_name = 'Publisher'
		begin
			set @Name = 'Publisher ' + convert(varchar(10), @i)
			set @Date = getdate()
			set @City = 'Origin ' + convert(varchar(10), @i)
			insert into Publisher(Name, FoundingDate, Origin) values (@Name, @Date, @City)
		end
		if @table_name = 'Product'
		begin
			set @Name = 'Console dev' + convert(varchar(10), @i)
			set @Date = getdate()
			set @City = 'Origin ' + convert(varchar(10), @i)
			set @Person = 'CEO ' + convert(varchar(10), @i)
			insert into ConsoleDeveloper(Name, Founding, Origin, CEO) values (@Name, @Date, @City, @Person)
			select @S_id = Cdid from ConsoleDeveloper where Name = @Name

			set @Name_m = 'Model ' + convert(varchar(10), @i)
			set @City2 = 'Origin ' + convert(varchar(10), @i)
			set @Date2 = getdate()
			set @Clients = 50 * @i
			insert into Consoles(Model, Origin, Release, Price) values (@Name_m, @City2, @Date2, @Clients)
			select @C_id = Cid from Consoles where Model = @Name_m

			insert into Product(Consoles_id, Console_dev_id) values (@S_id, @C_id)
		end
		set @i = @i + 1
	end
			
go

-- seleting rows
create or alter procedure delete_rows  @nb_rows int, @table_name varchar(50) as
	if @nb_rows < 1
		raiserror('Number or rows not valid !', 16, 1);

	declare @bottom_row int
	if @table_name = 'ShopManager'
	begin
		set @bottom_row = (select max(Smid) from ShopManager) - @nb_rows
		delete from ShopManager where Smid > @bottom_row
	end
	if @table_name = 'Publisher'
	begin
		set @bottom_row = (select max(Pid) from Publisher) - @nb_rows
		delete from Publisher where Pid > @bottom_row
	end
	if @table_name = 'Product'
	begin
		set @bottom_row = (select max(Prid) from Product) - @nb_rows
		delete from Product where Prid > @bottom_row
	end

go

-- evaluating views
create or alter procedure select_view @view_name varchar(10) as
	if @view_name = 'View1'
		select * from View1
	if @view_name = 'View2'
		select * from View2
	if @view_name = 'View3'
		select * from View3

go

create or alter procedure run_tests @nb_rows int as
	if @nb_rows < 1
		raiserror('Number or rows not valid !', 16, 1);
	-- get the times for the operaions

	-- insert data
	declare @ShopManager_insert_start datetime
	set @ShopManager_insert_start = getdate()
	exec insert_data @nb_rows, 'ShopManager'
	declare @ShopManager_insert_end datetime
	set @ShopManager_insert_end = getdate()

	declare @Publisher_insert_start datetime
	set @Publisher_insert_start = getdate()
	exec insert_data @nb_rows, 'Publisher'
	declare @Publisher_insert_end datetime
	set @Publisher_insert_end = getdate()

	declare @Product_insert_start datetime
	set @Product_insert_start = getdate()
	exec insert_data @nb_rows, 'Product'
	declare @Product_insert_end datetime
	set @Product_insert_end = getdate()
	
	-- delete data
	declare @Product_delete_start datetime
	set @Product_delete_start = getdate()
	exec delete_rows @nb_rows, 'Product'
	declare @Product_delete_end datetime
	set @Product_delete_end = getdate()

	declare @Publisher_delete_start datetime
	set @Publisher_delete_start = getdate()
	exec delete_rows @nb_rows, 'Publisher'
	declare @Publisher_delete_end datetime
	set @Publisher_delete_end = getdate()

	declare @ShopManager_delete_start datetime
	set @ShopManager_delete_start = getdate()
	exec delete_rows @nb_rows, 'ShopManager'
	declare @ShopManager_delete_end datetime
	set @ShopManager_delete_end = getdate()

	-- time for views
	declare @view1_start datetime
	set @view1_start = getdate()
	exec select_view 'View1'
	declare @view1_end datetime
	set @view1_end = getdate()

	declare @view2_start datetime
	set @view2_start = getdate()
	exec select_view 'View2'
	declare @view2_end datetime
	set @view2_end = getdate()

	declare @view3_start datetime
	set @view3_start = getdate()
	exec select_view 'View3'
	declare @view3_end datetime
	set @view3_end = getdate()

	-- tests
	declare @desc nvarchar(100)
	declare @last_id int

	set @desc = 'inserting ' + convert(varchar(10), @nb_rows) + ' rows...'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @ShopManager_insert_start, @ShopManager_insert_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunTables values (@last_id, 1, @ShopManager_insert_start, @ShopManager_insert_end)

	set @desc = 'inserting ' + convert(varchar(10), @nb_rows) + ' rows...'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @Publisher_insert_start, @Publisher_insert_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunTables values (@last_id, 2, @Publisher_insert_start, @Publisher_insert_end)

	set @desc = 'inserting ' + convert(varchar(10), @nb_rows) + ' rows...'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @Product_insert_start, @Product_insert_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunTables values (@last_id, 3, @Product_insert_start, @Product_insert_end)
	
	set @desc = 'deleting ' + convert(varchar(10), @nb_rows) + ' rows...'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @Product_delete_start, @Product_delete_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunTables values (@last_id, 3, @Product_delete_start, @Product_delete_end)

	set @desc = 'deleting ' + convert(varchar(10), @nb_rows) + ' rows...'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @Publisher_delete_start, @Publisher_delete_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunTables values (@last_id, 2, @Publisher_delete_start, @Publisher_delete_end)

	set @desc = 'deleting ' + convert(varchar(10), @nb_rows) + ' rows...'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @ShopManager_delete_start, @ShopManager_delete_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunTables values (@last_id, 1, @ShopManager_delete_start, @ShopManager_delete_end)

	set @desc = 'view 1'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @view1_start, @view1_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunViews values (@last_id, 1, @view1_start, @view1_end)

	set @desc = 'view 2'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @view2_start, @view2_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunViews values (@last_id, 2, @view2_start, @view2_end)

	set @desc = 'view 3'
	insert into TestRuns(Description, StartAt, EndAt) values (@desc, @view3_start, @view3_end)
	set @last_id = (select max(TestRunID) from TestRuns)
	insert into TestRunViews values (@last_id, 3, @view3_start, @view3_end)

	exec deleteAll @table_name = 'ShopManager'
    exec deleteAll @table_name = 'Publisher'
    exec deleteAll @table_name = 'Product'

go

exec run_tests @nb_rows = 10
exec insert_data @nb_rows = 10, @table_name = 'ShopManager'
exec delete_rows @nb_rows = 5, @table_name = 'Product'
exec deleteAll @table_name = 'ShopManager'
exec deleteAll @table_name = 'Publisher'
exec deleteAll @table_name = 'Product'
exec deleteAll @table_name = 'TestRuns'
exec deleteAll @table_name = 'TestRunViews'
exec deleteAll @table_name = 'TestRunTables'

select * from TestRuns
select * from TestRunTables
select * from TestRunViews


-- script code
if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunTables_Tables]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestRunTables] DROP CONSTRAINT FK_TestRunTables_Tables

GO


if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestTables_Tables]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestTables] DROP CONSTRAINT FK_TestTables_Tables

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunTables_TestRuns]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestRunTables] DROP CONSTRAINT FK_TestRunTables_TestRuns

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunViews_TestRuns]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestRunViews] DROP CONSTRAINT FK_TestRunViews_TestRuns

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestTables_Tests]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestTables] DROP CONSTRAINT FK_TestTables_Tests

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestViews_Tests]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestViews] DROP CONSTRAINT FK_TestViews_Tests

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunViews_Views]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestRunViews] DROP CONSTRAINT FK_TestRunViews_Views

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestViews_Views]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)

ALTER TABLE [TestViews] DROP CONSTRAINT FK_TestViews_Views

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[Tables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [Tables]

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[TestRunTables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [TestRunTables]

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[TestRunViews]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [TestRunViews]

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[TestRuns]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [TestRuns]

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[TestTables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [TestTables]

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[TestViews]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [TestViews]

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[Tests]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [Tests]

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[Views]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)

drop table [Views]

GO



CREATE TABLE [Tables] (

	[TableID] [int] IDENTITY (1, 1) NOT NULL ,

	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 

) ON [PRIMARY]

GO



CREATE TABLE [TestRunTables] (

	[TestRunID] [int] NOT NULL ,

	[TableID] [int] NOT NULL ,

	[StartAt] [datetime] NOT NULL ,

	[EndAt] [datetime] NOT NULL 

) ON [PRIMARY]

GO



CREATE TABLE [TestRunViews] (

	[TestRunID] [int] NOT NULL ,

	[ViewID] [int] NOT NULL ,

	[StartAt] [datetime] NOT NULL ,

	[EndAt] [datetime] NOT NULL 

) ON [PRIMARY]

GO



CREATE TABLE [TestRuns] (

	[TestRunID] [int] IDENTITY (1, 1) NOT NULL ,

	[Description] [nvarchar] (2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,

	[StartAt] [datetime] NULL ,

	[EndAt] [datetime] NULL 

) ON [PRIMARY]

GO



CREATE TABLE [TestTables] (

	[TestID] [int] NOT NULL ,

	[TableID] [int] NOT NULL ,

	[NoOfRows] [int] NOT NULL ,

	[Position] [int] NOT NULL 

) ON [PRIMARY]

GO



CREATE TABLE [TestViews] (

	[TestID] [int] NOT NULL ,

	[ViewID] [int] NOT NULL 

) ON [PRIMARY]

GO



CREATE TABLE [Tests] (

	[TestID] [int] IDENTITY (1, 1) NOT NULL ,

	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 

) ON [PRIMARY]

GO



CREATE TABLE [Views] (

	[ViewID] [int] IDENTITY (1, 1) NOT NULL ,

	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 

) ON [PRIMARY]

GO



ALTER TABLE [Tables] WITH NOCHECK ADD 

	CONSTRAINT [PK_Tables] PRIMARY KEY  CLUSTERED 

	(

		[TableID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [TestRunTables] WITH NOCHECK ADD 

	CONSTRAINT [PK_TestRunTables] PRIMARY KEY  CLUSTERED 

	(

		[TestRunID],

		[TableID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [TestRunViews] WITH NOCHECK ADD 

	CONSTRAINT [PK_TestRunViews] PRIMARY KEY  CLUSTERED 

	(

		[TestRunID],

		[ViewID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [TestRuns] WITH NOCHECK ADD 

	CONSTRAINT [PK_TestRuns] PRIMARY KEY  CLUSTERED 

	(

		[TestRunID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [TestTables] WITH NOCHECK ADD 

	CONSTRAINT [PK_TestTables] PRIMARY KEY  CLUSTERED 

	(

		[TestID],

		[TableID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [TestViews] WITH NOCHECK ADD 

	CONSTRAINT [PK_TestViews] PRIMARY KEY  CLUSTERED 

	(

		[TestID],

		[ViewID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [Tests] WITH NOCHECK ADD 

	CONSTRAINT [PK_Tests] PRIMARY KEY  CLUSTERED 

	(

		[TestID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [Views] WITH NOCHECK ADD 

	CONSTRAINT [PK_Views] PRIMARY KEY  CLUSTERED 

	(

		[ViewID]

	)  ON [PRIMARY] 

GO



ALTER TABLE [TestRunTables] ADD 

	CONSTRAINT [FK_TestRunTables_Tables] FOREIGN KEY 

	(

		[TableID]

	) REFERENCES [Tables] (

		[TableID]

	) ON DELETE CASCADE  ON UPDATE CASCADE ,

	CONSTRAINT [FK_TestRunTables_TestRuns] FOREIGN KEY 

	(

		[TestRunID]

	) REFERENCES [TestRuns] (

		[TestRunID]

	) ON DELETE CASCADE  ON UPDATE CASCADE 

GO



ALTER TABLE [TestRunViews] ADD 

	CONSTRAINT [FK_TestRunViews_TestRuns] FOREIGN KEY 

	(

		[TestRunID]

	) REFERENCES [TestRuns] (

		[TestRunID]

	) ON DELETE CASCADE  ON UPDATE CASCADE ,

	CONSTRAINT [FK_TestRunViews_Views] FOREIGN KEY 

	(

		[ViewID]

	) REFERENCES [Views] (

		[ViewID]

	) ON DELETE CASCADE  ON UPDATE CASCADE 

GO



ALTER TABLE [TestTables] ADD 

	CONSTRAINT [FK_TestTables_Tables] FOREIGN KEY 

	(

		[TableID]

	) REFERENCES [Tables] (

		[TableID]

	) ON DELETE CASCADE  ON UPDATE CASCADE ,

	CONSTRAINT [FK_TestTables_Tests] FOREIGN KEY 

	(

		[TestID]

	) REFERENCES [Tests] (

		[TestID]

	) ON DELETE CASCADE  ON UPDATE CASCADE 

GO



ALTER TABLE [TestViews] ADD 

	CONSTRAINT [FK_TestViews_Tests] FOREIGN KEY 

	(

		[TestID]

	) REFERENCES [Tests] (

		[TestID]

	),

	CONSTRAINT [FK_TestViews_Views] FOREIGN KEY 

	(

		[ViewID]

	) REFERENCES [Views] (

		[ViewID]

	)

GO