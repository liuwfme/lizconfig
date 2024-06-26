create table if not exists `configs` (
    `app` varchar (64) not null,
    `env` varchar (64) null,
    `namespace` varchar (64) null,
    `pkey` varchar (64) null,
    `pval` varchar (128) null
);

insert into configs (app, env, namespace, pkey, pval) values ('app1', 'dev', 'public', 'a', '1.0.0');
insert into configs (app, env, namespace, pkey, pval) values ('app1', 'dev', 'public', 'b', 'http://localhost:9129');
insert into configs (app, env, namespace, pkey, pval) values ('app1', 'dev', 'public', 'c', 'c100');

create table if not exists `locks` (
    `id` int primary key not null,
    `app` varchar (64) null
);

insert into locks(id, app) values (1, 'lizconfig-server');


