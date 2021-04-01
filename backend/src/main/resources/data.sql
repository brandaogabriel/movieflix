-- Genres
INSERT INTO tb_genre (name, create_moment, status)
VALUES ('aventura', NOW(), true);
INSERT INTO tb_genre (name, create_moment, status)
VALUES ('terror', NOW(), true);
INSERT INTO tb_genre (name, create_moment, status)
VALUES ('romance', NOW(), true);

-- Users
INSERT INTO tb_user (name, email, password, create_moment, status)
VALUES ('Bob', 'bob@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW(), true);
INSERT INTO tb_user (name, email, password, create_moment, status)
VALUES ('Ana', 'ana@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW(), true);
INSERT INTO tb_user (name, email, password, create_moment, status)
VALUES ('Admin', 'admin@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW(), true);

-- Roles
INSERT INTO tb_role (authority)
VALUES ('ROLE_VISITOR');
INSERT INTO tb_role (authority)
VALUES ('ROLE_MEMBER');
INSERT INTO tb_role (authority)
VALUES ('ROLE_ADMIN');

-- Users roles
INSERT INTO tb_user_role (user_id, role_id)
VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id)
VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id)
VALUES (3, 1);
INSERT INTO tb_user_role (user_id, role_id)
VALUES (3, 2);
INSERT INTO tb_user_role (user_id, role_id)
VALUES (3, 3);

-- MOVIES
-- Genero: Aventura
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/aSy0zROlkSSJEcHj7UvdvPOaMpq.jpg',
        'Prepara-se para a maior aventura!',
        'Ladybug é uma heroína que tem a missão de defender Paris de um vilão misterioso. Junto com o parceiro Cat Noir, eles devem conciliar o dia a dia com a vida de super-heróis.',
        'Miraculous: As aventuras de LadyBug', 2015, 1, NOW(), true);

-- Genero: Aventura
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/gLIDdvf3vmYmZ9RPzEfGUKQqbDO.jpg',
        'A grande aventura',
        'O Professor Bactério (Janfri Topera) criou sua principal invenção, o DDT (Desmoralizador de Tropas), que pode também ser uma perigosa arma se cair em mãos erradas. É justamente o que acontece, quando a invenção é roubada por Nadiusko (Janusz Ziemniak), a mando do ditador da Tirania. Para recuperar o artefato é enviado Fredy Mazas (Dominique Pinon), o detetive mais qualificado da atualidade, que viaja disfarçado. Quando a agência secreta TIA recebe a notícia de que Mazas morreu, em seu lugar são enviados os atrapalhados agentes Mortadelo (Benito Pocino) e Salaminho (Pepe Viyuela), que com a ajuda de Ofelia (Berta Ojea) precisarão recuperar a invenção.',
        'Mortadelo E Salaminho - Agentes Quase Secretos', 2003, 1, NOW(), true);

-- Genero: Aventura
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/eJg9hgXdD8KLe8G4ovXhljWm952.jpg',
        'Amazônia adentro',
        'Anos se passaram e a indiazinha Tainá (Eunice Baía) cresceu. Agora uma pré-adolescente, ela se divide entre o enfrentamento com os bandidos e a atenção que dá a uma nova amiga. A pequenina Catiti, de 6 anos, fugiu da sua aldeia na tentativa de imitar Tainá como protetora do meio ambiente. Juntas, elas vão viver muitas aventuras na floresta.',
        'Tainá 2 - A aventura continua', 2004, 1, NOW(), true);

-- Genero: Terror
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/j6Er72CMHKiZgFr0HBMbuyj7Ssa.jpg',
        'Cuidado com a casa',
        'Pouco antes do Natal, a jovem Zoe, seu irmão Franklin e sua família se mudam para uma mansão isolada com um passado sombrio. Enquanto os adultos se concentram na renovação do local, as crianças entediadas encontram um baú de brinquedos escondido no sótão e ficam maravilhados quando os brinquedos lá dentro ganham vida. Mas eventos bizarros logo começam a acontecer – eventos que ameaçam a vida da família. Conforme o dia especial amanhece com presentes empilhados sob a árvore, a contagem de corpos aumenta e o sangue começa a fluir. Este “conto assustador” original vai fazer as crianças gritarem pelas férias!',
        'Brinquedos de Terror', 2020, 2, NOW(), true);

-- Genero: Terror
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/bUmaGlXAtAizyo6zArOP2YDVKZB.jpg',
        'Humanity"s last hope… rests on a high power machine gun!',
        'Uma experiência fracassada libera vírus que transforma humanos em zumbis sedentos de sangue e carnívoros. El Wray, Cherry, um médico e o xerife unem forças para sobreviver à noite, quando os mutantes ameaçam tomar a cidade e o mundo.',
        'Planeta Terror', 2007, 2, NOW(), true);

-- Genero: Terror
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/5G9e8dJaxQ2KUaPOGeQ1wA4QdlN.jpg',
        'The boys and girls of Sigma Phi. Some will live. Some will die.',
        'Na véspera do ano novo, os rapazes da Sigma Phi convidam os amigos para uma festa à fantasia no interior de um trem, sem saber que entre eles se esconde um terrível assassino',
        'O Trem do Terror', 1980, 2, NOW(), true);

-- Genero: Romance
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/4RbxxPAUushqUYNO8EW09GSluBt.jpg',
        'Amantes em apuro',
        'Musical sobre homem que arranja uma amante bem mais jovem e decide manter o relacionamento extraconjugal. Mas, quando descoberto, busca reconquistar sua mulher e seus filhos',
        'Romance e Cigarros', 2005, 3, NOW(), true);

-- Genero: Romance
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/y2CsuILovvfVOmZ3MEbik2okd1a.jpg',
        'Amor com chocolate',
        'Dispensada pelo namorado, Emma (Lacey Chabert) decide fazer uma viagem para a Bélgica, onde conhece o charmoso chocolateiro Luc Simon (Will Kemp). Emma então descobre uma paixão pela gastronomia e vive um intenso romance.',
        'Amor, Romance e Chocolate', 2019, 3, NOW(), true);

-- Genero: Romance
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/usYXRWXnPGbtbJszK8Pzcl35Hvv.jpg',
        'Curtindo o amor nas ferias',
        'Uma estrela de reality show sai de Hollywood e parte em direção a Amish Country, onde ela deve decidir entre viver sua nova realidade ou voltar para a antiga.',
        'Entre o Amor e a Fama', 2018, 3, NOW(), true);

-- Genero: Romance
INSERT INTO tb_movie (img_url, sub_title, synopsis, title, year, genre_id, create_moment, status)
VALUES ('https://www.themoviedb.org/t/p/w533_and_h300_bestv2/66z2nfpzKTokJ5i8mW5hpJa12Yd.jpg',
        'Just when you think you''ve found the right guy, someone even worse comes along.',
        'Uma divorciada com 33 anos, Emma Moriarity (Sally Field), se muda com seu filho de 12 anos, Jake (Corey Haim), para um lugarejo do Arizona, mais exatamente o Rancho Wilcox, que está decadente. Ela pretende levantar o lugar e recomeçar sua vida adestrando cavalos, algo que entende realmente. Logo faz amizade com um viúvo, Murphy Jones (James Garner), que é bem mais velho que ela mas isto não o impede de ter uma vida social bem ativa, pois existem 10 mulheres para cada homem na localidade. Eles são vistos juntos cada vez mais e, apesar de nada ter acontecido entre eles, tudo indica que é uma questão de tempo. Entretanto o aparecimento súbito de Bobby Jack (Brian Kerwin), o ex-marido dela, pode ser um empecilho. Sem dinheiro e sem emprego, Bobby não tem onde morar e então fica no rancho. Bobby quer Emma de volta, mas ela nem pensa no assunto, enquanto que Jake adora o pai e está feliz com a presença dele no rancho.',
        'O Romance de Murphy', 1985, 3, NOW(), true);

-- Reviews
INSERT INTO tb_review (text, movie_id, user_id, create_moment, status)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tincidunt vulputate interdum. Sed eget erat mollis, fringilla magna eget, viverra metus. Aliquam in fermentum purus. Vivamus eu magna sodales, mollis ipsum id, maximus nisi. Ut posuere efficitur ligula nec molestie. Suspendisse in vehicula neque. Mauris metus mi, mattis eget sapien et, pharetra aliquet leo. Curabitur lobortis augue orci, id aliquam mauris fringilla non. Maecenas in posuere tortor, sit amet dictum mauris. Sed non porta ante, in vehicula sem.',
        1, 1, NOW(), true);

INSERT INTO tb_review (text, movie_id, user_id, create_moment, status)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tincidunt vulputate interdum. Sed eget erat mollis, fringilla magna eget, viverra metus. Aliquam in fermentum purus. Vivamus eu magna sodales, mollis ipsum id, maximus nisi. Ut posuere efficitur ligula nec molestie. Suspendisse in vehicula neque. Mauris metus mi, mattis eget sapien et, pharetra aliquet leo. Curabitur lobortis augue orci, id aliquam mauris fringilla non. Maecenas in posuere tortor, sit amet dictum mauris. Sed non porta ante, in vehicula sem.',
        2, 1, NOW(), true);

INSERT INTO tb_review (text, movie_id, user_id, create_moment, status)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tincidunt vulputate interdum. Sed eget erat mollis, fringilla magna eget, viverra metus. Aliquam in fermentum purus. Vivamus eu magna sodales, mollis ipsum id, maximus nisi. Ut posuere efficitur ligula nec molestie. Suspendisse in vehicula neque. Mauris metus mi, mattis eget sapien et, pharetra aliquet leo. Curabitur lobortis augue orci, id aliquam mauris fringilla non. Maecenas in posuere tortor, sit amet dictum mauris. Sed non porta ante, in vehicula sem.',
        3, 2, NOW(), true);

INSERT INTO tb_review (text, movie_id, user_id, create_moment, status)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tincidunt vulputate interdum. Sed eget erat mollis, fringilla magna eget, viverra metus. Aliquam in fermentum purus. Vivamus eu magna sodales, mollis ipsum id, maximus nisi. Ut posuere efficitur ligula nec molestie. Suspendisse in vehicula neque. Mauris metus mi, mattis eget sapien et, pharetra aliquet leo. Curabitur lobortis augue orci, id aliquam mauris fringilla non. Maecenas in posuere tortor, sit amet dictum mauris. Sed non porta ante, in vehicula sem.',
        5, 2, NOW(), true);

INSERT INTO tb_review (text, movie_id, user_id, create_moment, status)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tincidunt vulputate interdum. Sed eget erat mollis, fringilla magna eget, viverra metus. Aliquam in fermentum purus. Vivamus eu magna sodales, mollis ipsum id, maximus nisi. Ut posuere efficitur ligula nec molestie. Suspendisse in vehicula neque. Mauris metus mi, mattis eget sapien et, pharetra aliquet leo. Curabitur lobortis augue orci, id aliquam mauris fringilla non. Maecenas in posuere tortor, sit amet dictum mauris. Sed non porta ante, in vehicula sem.',
        3, 2, NOW(), true);

INSERT INTO tb_review (text, movie_id, user_id, create_moment, status)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam tincidunt vulputate interdum. Sed eget erat mollis, fringilla magna eget, viverra metus. Aliquam in fermentum purus. Vivamus eu magna sodales, mollis ipsum id, maximus nisi. Ut posuere efficitur ligula nec molestie. Suspendisse in vehicula neque. Mauris metus mi, mattis eget sapien et, pharetra aliquet leo. Curabitur lobortis augue orci, id aliquam mauris fringilla non. Maecenas in posuere tortor, sit amet dictum mauris. Sed non porta ante, in vehicula sem.',
        2, 2, NOW(), true);