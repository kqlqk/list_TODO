delete from users;
delete from roles;

insert into roles (id, name)
    values (1, 'ROLE_USER');
insert into roles (id, name)
    values (2, 'ROLE_ADMIN');

insert into refresh_tokens (id, token, expires_in)
values (1,--for user
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQG1haWwuY29tIiwiaWF0IjoxNjYwNDkzMzEzLCJleHAiOjk2NjMwODUzMTN9._dP6FpQNAkT7fX8KTeQ5JwCdZEosvSszTHRJ9a5lelA',
        '9663085313');
insert into refresh_tokens (id, token, expires_in)
    values (2,--for admin
            'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTY2MDQ5MzMxMywiZXhwIjo5NjYzMDg1MzEzfQ.wq9dNvkYwL_fcw0J7u0LEOn_o4cyMsHP0gjn-AWdb0Q',
            '9663085313');

insert into users (id, email, login, password, role_id, oauth2, refresh_token_id)
values (1,
        'user@mail.com',
        'userLogin',
        '$2a$12$z4rjEoKil9O13cmzjYFGNeKcvijn83xYuSA8wOe9ahDP2MuDJnBqG', --User1234
        1,
        false,
        1);

insert into users (id, email, login, password, role_id, oauth2, refresh_token_id)
values (2,
        'admin@mail.com',
        'adminLogin',
        '$2a$12$aR9AGELCD2DXMdloPof1IegRJYYWehFDmRovLQ.1lIlerdeASaZjy', --Admin1234
        2,
        false,
        2);

insert into notes (id, title, body, last_edited, user_id, full_title)
    values (1,
            'anyTitle',
            null,
            default,
            1,
            'anyTitle');
insert into notes (id, title, body, last_edited, user_id, full_title)
    values (2,
            'anySecondTitle',
            'any body',
            default,
            1,
            'anySecondTitle');
insert into notes (id, title, body, last_edited, user_id, full_title)
values (3,
        'anyThirdTitle',
        null,
        default,
        2,
        'anyThirdTitle');
