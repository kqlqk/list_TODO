delete from users;
delete from roles;

insert into roles (name)
    values ('ROLE_USER');
insert into roles (name)
    values ('ROLE_ADMIN');


insert into refresh_tokens (token, expires_in)
values ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQG1haWwuY29tIiwiaWF0IjoxNjYwNDkzMzEzLCJleHAiOjk2NjMwODUzMTN9._dP6FpQNAkT7fX8KTeQ5JwCdZEosvSszTHRJ9a5lelA',
        '9663085313');--for user
insert into refresh_tokens (token, expires_in)
    values ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTY2MDQ5MzMxMywiZXhwIjo5NjYzMDg1MzEzfQ.wq9dNvkYwL_fcw0J7u0LEOn_o4cyMsHP0gjn-AWdb0Q',
            '9663085313');--for admin


insert into users (email, login, password, role_id, oauth2, refresh_token_id)
values ('user@mail.com',
        'userLogin',
        '$2a$12$z4rjEoKil9O13cmzjYFGNeKcvijn83xYuSA8wOe9ahDP2MuDJnBqG', --User1234
        1,
        false,
        1);

insert into users (email, login, password, role_id, oauth2, refresh_token_id)
values ('admin@mail.com',
        'adminLogin',
        '$2a$12$aR9AGELCD2DXMdloPof1IegRJYYWehFDmRovLQ.1lIlerdeASaZjy', --Admin1234
        2,
        false,
        2);


insert into notes (title, body, last_edited, user_id, full_title)
    values ('anyTitle',
            null,
            default,
            1,
            'anyTitle');
insert into notes (title, body, last_edited, user_id, full_title)
values ('anySecondTitle',
        'any body',
        default,
        1,
        'anySecondTitle');
insert into notes (title, body, last_edited, user_id, full_title)
values ('anyThirdTitle',
        null,
        default,
        2,
        'anyThirdTitle');

