--creating the table
--public class Book {
--    private Long id;
--    private String isbn;
--    private String name;
--    private boolean isRare;
--    private int numberOfPages;
--}
drop table book;
create table book
(
--	id bigint unique not null,
    id              bigint primary key,
--same as the upper one
    isbn            varchar(13),
    name            varchar(199),
    is_rare         bool    default false,
    number_of_pages integer default 1 check (number_of_pages > 1)
);


--creating the table
--public class Page {
--    private Long id;
--    private String content;
--    private int pageNumber;
--
--    private Book book;
--}
drop table page;
create table page
(
    id          bigint primary key,
    content     text    not null,
    page_number integer not null,
    book_id     bigint references book (id) --foreign key references the primary key from other table (book)
);


--altering
alter table page
    add column foo bool;
alter table page
    drop column foo;
alter table page
    add constraint page_number check (page_number > -1);
alter table page
    drop constraint page_number;
ALTER TABLE public.page
    ALTER COLUMN page_number DROP NOT NULL;

--manipulating data
insert into book(id, isbn, "name", is_rare, number_of_pages)
values (1, '456432132133', 'The Book', true, 375),
       (2, '456432132133', 'The Book 2', false, 189),
       (3, '456432132133', 'The Book 3', true, 400);
insert into book(id, number_of_pages)
values (4, 375);

insert into page(id, "content", page_number, book_id)
values (1, '1 bla bla bla', 1, 1),
       (2, '1 bla bla bla', 2, 1),
       (3, '1 bla bla bla', 3, 1),
       (4, '2 bla bla bla', 1, 2),
       (5, '3 bla bla bla', 1, 3),
       (6, '3 bla bla bla', 2, 3);

update book
set isbn = '7894655130'
where id = 4;
update book
set "name" = 'The Greatest Book'
where isbn = '7894655130';
--update book set number_of_pages = 0; sets all the records number of pages to 0 due to the missing where clause
--delete from book; deletes all the records from book due to the missing where clause
delete
from book
where id = 4;

select *
from book;
select id, isbn, is_rare
from book;

select *
from book
where number_of_pages > 300;
select *
from book
where is_rare = true;
select *
from book
where id = 1;


--joining
-- * inner join
select *
from book b
         join page p on b.id = p.book_id;
select b."name", p."content", p.page_number
from book b
         join page p on b.id = p.book_id;

insert into book(id, isbn, "name", is_rare, number_of_pages)
values (99, '456432132133', 'The Book 99', true, 375);

-- * left outer join
select *
from book b
         left join page p on b.id = p.book_id;


insert into page(id, "content", book_id)
values (9, '1 bla bla bla', 1);

-- * right outer join
--don`t do this, this is stupid, examples purposes only
select *
from book b
         right join page p on b.id = p.page_number;

-- * full outer join
select *
from book b
         full join page p on b.id = p.book_id;
--don`t do this, this is stupid, examples purposes only
select *
from book b
         full join page p on b.id = p.page_number;

-- * cross join
select *
from book b
         cross join page p;


--where

select *
from book
where id in (1, 2);

select *
from book
where id in (select book_id from page where page_number > 1);

select distinct b.id, b."name", p."content"
from book b
         join page p on b.id = p.book_id;

--combining
--union
select *
from book b
where b."name" = 'The Book 99'
union
select *
from book b
where b.is_rare = true;

--intersect
select *
from book b
where b."name" = 'The Book 3'
intersect
select *
from book b
where b.is_rare = true;

--except
select *
from book b
where b.is_rare = true
    except
select *
from book b
where b."name" = 'The Book 3';


--union
select *
from book b
         join page p ON b.id = p.book_id
where b."name" = 'The Book 99'
union
select *
from book b
         join page p ON b.id = p.book_id
where b.is_rare = true;

--intersect
select *
from book b
         join page p ON b.id = p.book_id
where b."name" = 'The Book 3'
intersect
select *
from book b
         join page p ON b.id = p.book_id
where b.is_rare = true;

--except
select *
from book b
         join page p ON b.id = p.book_id
where b.is_rare = true
    except
select *
from book b
         join page p ON b.id = p.book_id
where b."name" = 'The Book 3';

--combining
--union
select b.id, b."name"
from book b
where b."name" = 'The Book 99'
union
select b.id, b."name"
from book b
where b.is_rare = true;

--intersect
select b.id, b."name"
from book b
where b."name" = 'The Book 3'
intersect
select b.id, b."name"
from book b
where b.is_rare = true;

--except
select b.id, b."name"
from book b
where b.is_rare = true
    except
select b.id, b."name"
from book b
where b."name" = 'The Book 3';

--union
select b.id
from book b
where b."name" = 'The Book 99'
union
select p.id
from page p
where p.page_number >= 1;

--intersect
select b.id
from book b
where b."name" = 'The Book 3'
intersect
select p.id
from page p
where p.page_number >= 1;

--except
select p.id
from page p
where p.page_number >= 1
    except
select b.id
from book b
where b."name" = 'The Book 3';

--limit & offset
select *
from page p;
select *
from page p
limit 5;
select *
from page p
offset 5;
select *
from page p
offset 3 limit 2;
select *
from page p
offset 10 limit 10;
--ignores out of bounds

--logical and comparison operators
select *
from book b
where b.is_rare = true
  and b.number_of_pages > 375;
select *
from book b
where b.is_rare = true
  and b.number_of_pages <> 375; -- pay attention, no == and != in SQL, = and <>
select *
from book b
where b."name" = 'The Book'
   or b.isbn is null;
select *
from book b
where b."name" = 'The Book'
   or b.isbn is not null;


--pattern matching
select *
from book b
where b.name like '%The%';
select *
from book b
where b.name like 'The B%';
select *
from book b
where b.name like '%3';

select *
from book b;

select *
from page p;

select *
from page
where book_id = 4;

--about auto increment id
select nextval('book_sequence');
insert into book(isbn, "name", is_rare, number_of_pages)
values ('456432132133', 'The Book', true, 375),
       ('456432132133', 'The Book 2', false, 189),
       ('456432132133', 'The Book 3', true, 400);

insert into page("content", page_number, book_id)
values ('1 bla bla bla', 1, 18),
       ('1 bla bla bla', 2, 18),
       ('1 bla bla bla', 3, 18),
       ('2 bla bla bla', 1, 18),
       ('3 bla bla bla', 1, 18),
       ('3 bla bla bla', 2, 18);





