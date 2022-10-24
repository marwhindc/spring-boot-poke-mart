-- user table
INSERT INTO users (user_id, user_name)
VALUES
    (1, 'Ash Ketchum')
	(2, 'Misty Williams');

-- products table
INSERT INTO products (product_id, product_name, product_description, product_price, image_url)
VALUES
    (1, 'Poke Ball', 'The PokéBall is the standard PokéBall you can obtain. It has a 1x Capture rate that doesn''t increase the chances of capturing a Pokémon.', 200, 'https://archives.bulbagarden.net/media/upload/7/79/Dream_Poké_Ball_Sprite.png'),
    (2, 'Great Ball', 'The Great Ball is a standard PokéBall you can obtain. It has a 1.5x Capture rate that increases the likelihood of capturing a Pokémon.', 600, 'https://archives.bulbagarden.net/media/upload/b/bf/Dream_Great_Ball_Sprite.png'),
    (3, 'Dusk Ball', 'The Dusk Ball is a PokéBall that is more effective within Dark enviornments. Within Caves or at night-time, the Capture Rate is increased by 3x.', 1000, 'https://archives.bulbagarden.net/media/upload/5/59/Dream_Dusk_Ball_Sprite.png'),
    (4, 'Luxury Ball', 'The Luxury Ball is a PokéBall that has no added Capture Rate. Instead, it helps increase the Pokémon that is within the Pokéball''s happiness.', 1000, 'https://archives.bulbagarden.net/media/upload/7/7e/Dream_Luxury_Ball_Sprite.png'),
    (5, 'Timer Ball', 'The Timer Ball is a PokéBall that works best on Pokémon as the duration of battle increases.', 1000, 'https://archives.bulbagarden.net/media/upload/f/f0/Dream_Timer_Ball_Sprite.png'),
    (6, 'Ultra Ball', 'The Ultra Ball is a standard PokéBall you can obtain. It has a 2x Capture rate that increases the likelihood of capturing a Pokémon.', 1200, 'https://archives.bulbagarden.net/media/upload/a/a8/Dream_Ultra_Ball_Sprite.png');

-- carts table (temp data)
INSERT INTO carts (cart_id, user_id)
VALUES
    (1, 1);

--cart_items table (temp data)
INSERT INTO cart_items (cart_item_id, cart_id, product_id, quantity)
VALUES
    (1, 1, 1, 5),
	(2, 1, 2, 2);