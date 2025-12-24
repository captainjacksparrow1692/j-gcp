INSERT INTO users (
    full_name,
    address,
    phone_number,
    email,
    photo_url,
    pinfl,
    age,
    document_type,
    issue_date,
    expiry_date,
    citizenship,
    death_date
)
SELECT
    'User ' || gs,
    'Tashkent, street ' || (gs % 5000) || ', house ' || (gs % 300),
    '+998' || (900000000 + (gs % 100000000)),
    'user' || gs || '@mail.com',
    CASE WHEN random() < 0.7 THEN NULL ELSE 'https://pics.example/' || gs || '.jpg' END,
    LPAD(gs::text, 14, '0'),
    18 + (gs % 63),
    CASE gs % 3
    WHEN 0 THEN 'PASSPORT'
        WHEN 1 THEN 'ID_CARD'
        ELSE 'DRIVER_LICENSE'
END,
    DATE '2010-01-01' + (gs % 5000),
    DATE '2010-01-01' + (gs % 5000) + 3650,
    CASE gs % 5
        WHEN 0 THEN 'Uzbekistan'
        WHEN 1 THEN 'Russia'
        WHEN 2 THEN 'Kazakhstan'
        WHEN 3 THEN 'Kyrgyzstan'
        ELSE 'Tajikistan'
END,
    CASE WHEN random() < 0.97 THEN NULL ELSE DATE '2015-01-01' + (gs % 3000) END
FROM generate_series(1, 100000) gs;
