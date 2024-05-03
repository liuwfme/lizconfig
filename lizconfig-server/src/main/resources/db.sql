create table if not exists `configs` (
    `app` varchar (64) not null,
    `env` varchar (64) null,
    `namespace` varchar (64) null,
    `pkey` varchar (64) null,
    `pval` varchar (128) null
);

insert into configs (app, env, namespace, pkey, pval) values ('app1', 'dev', 'public', 'a', '1.0.0');
insert into configs (app, env, namespace, pkey, pval) values ('app1', 'dev', 'public', 'b', 'http://localhost:9192');
insert into configs (app, env, namespace, pkey, pval) values ('app1', 'dev', 'public', 'c', 'cc100');

