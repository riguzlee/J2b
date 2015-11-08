delete from SHIRO;
delete from `USER`;

insert into SHIRO (SHIRO_ID, NAME, `ORDER`, URL, FILTERS, FROM_DATE, CREATED_DATE)
	values
('01', '静态资源(CSS,JS..)', 0    , '/static/**',             'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11') ),
('02', '验证码',             0    , '/randpic/**',            'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11')),
('03', '首页',               10   , '/index',                 'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11')),
('04', '登录页面',           10   , '/login',                 'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11')),
('05', '登录请求',           10   , '/doLogin',               'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11')),
('06', '发送重置密码请求',   10   , '/sendReset',             'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11')),
('07', '重置密码请求',       10   , '/reset',                 'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11')),
('08', '重置密码请求',       10   , '/doReset',               'anon', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11')),
('1099', '其他页面',         99999, '/**',                    'authc,user', ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11'));

insert into `USER` (USER_ID, LOGIN_NAME, PASSWORD, EMAIL, FAIL_TOTAL, ACCOUNT_STATUS, EMAIL_STATUS, FROM_DATE, CREATED_DATE)
    values
('01', 'admin', '$2a$12$m1NM1L4urhLSb25y/mi1COjLrV7l8tEFviLuOIuPc/aq4H7.9eZ9S', '1125482715@qq.com', 0, 0, 0, ('2015-11-11 11:11:11') ,('2015-11-11 11:11:11'));