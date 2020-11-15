
-- change column from varchar type to int type
create procedure modif_type
as
	alter table Genre 
	alter column Type int;
go

-- change the type back to varchar
create procedure modif_type_undo
as
	alter table Genre
	alter column Type varchar(50)
go

-- add a column
create procedure add_col
as
	alter table OnlinePlatforms
	add size int
go

-- remove a column
create procedure remove_col
as
	alter table OnlinePlatforms
	drop column size
go

-- add a default constraint
create procedure add_constraint
as
	alter table Shop
	add constraint shop_constraint default 'defalult city' for city
go
-- remove a default constrain
create procedure remove_constraint
as
	alter table Shop
	drop constraint shop_constraint
go

-- add primary key
create procedure add_pkey
as
	alter table Shop
	add constraint Shop_VideoGame_pk primary key (SVid)
go

-- remove primary key
create procedure remove_pkey
as
	alter table Shop
	drop constraint Shop_VideoGame_pk
go

-- add candidate key
create procedure add_ckey
as
	alter table Publisher 
	add constraint Publisher_ck unique (Name)
go

-- remove candidate key
create procedure remove_ckey
as
	alter table Publisher 
	drop constraint Publisher_ck
go

-- add foreign key
create procedure add_fkey
as
	alter table Shop
	add constraint Shop_Game_fk foreign key (Vid) references VideoGame
go

-- remove foreign key
create procedure remove_fkey
as
	alter table Shop
	drop constraint Shop_Game_fk
go 

-- create table
create procedure add_table
as
	create table Artists
	(Aid int primary key,
	Name varchar(20),
	project_portfolio varchar(50)
	)
go

-- create table
create procedure remove_table
as
	drop table Artists
go

-- next version
create procedure next_version @ver int
as
	if @ver = 1
		exec modif_type
	else if @ver = 2
		exec add_col
	else if @ver = 3
		exec add_constraint
	else if @ver = 4
		exec add_pkey
	else if @ver = 5
		exec add_ckey
	else if @ver = 6
		exec add_fkey
	else if @ver = 7
		exec add_table
go

-- prev version
create procedure prev_version @vrs int
as
	if @vrs = 1
		exec modif_type_undo
	else if @vrs = 2
		exec remove_col
	else if @vrs = 3
		exec remove_constraint
	else if @vrs = 4
		exec remove_pkey
	else if @vrs = 5
		exec remove_ckey
	else if @vrs = 6
		exec remove_fkey
	else if @vrs = 7
		exec remove_table
go

create procedure get_version @v int output
as
	select @v = v.version from version v
go

create procedure update_history @old int, @new int
as
	insert into Version_history values (@old, @new)
go

create procedure update_version @vr int
as
	update Version 
	set version = @vr
go

create procedure change_version @version int
as
	declare @curr_ver int = 1;
	declare @src int = @curr_ver
	exec get_version @v = @curr_ver output

	if @src < @version
		while @src < @version
		begin
			exec next_version @ver = @src;
			set @src = @src + 1
		end
	else 
		while @src > @version
		begin
			exec prev_version @vrs = @src;
			set @src = @src - 1
		end

	exec update_version @vr = @version
	exec update_history @old = @curr_ver, @new = @version

go


exec change_version @version = 0
create table Version_history (
id int primary key,
old_version int,
new_version int
)
create table Version ( version int )
insert into Version(version) values (0);

