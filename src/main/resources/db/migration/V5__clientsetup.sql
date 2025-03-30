INSERT INTO client (
    id,
    client_id,
    client_id_issued_at,
    client_secret,
    client_secret_expires_at,
    client_name,
    client_authentication_methods,
    authorization_grant_types,
    redirect_uris,
    post_logout_redirect_uris,
    scopes,
    client_settings,
    token_settings
)
VALUES (
    'acf17d5c-8747-4849-a815-430930466f1b',
    'scaler',
    '2025-03-30 10:06:10',
    '$2a$12$qI9/zPEx2tITCJwGI6ni7u.9VEUxcerU2xi3YU7vsACcwomy4A9JK',
    NULL,
    'acf17d5c-8747-4849-a815-430930466f1b',
    'client_secret_basic',
    'refresh_token,authorization_code',
    'https://oauth.pstmn.io/v1/callback',
    'https://oauth.pstmn.io/v1/callback',
    'ADMIN',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}'
);