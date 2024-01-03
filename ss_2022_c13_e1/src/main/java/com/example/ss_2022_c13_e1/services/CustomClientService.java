package com.example.ss_2022_c13_e1.services;

import com.example.ss_2022_c13_e1.entities.Client;
import com.example.ss_2022_c13_e1.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomClientService implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        Client client = Client.builder()
                .clientId(registeredClient.getClientId())
                .secret(registeredClient.getClientSecret())
                .scope(String.join(",", registeredClient.getScopes()))
                .authMethod(String.join(",", registeredClient.getClientAuthenticationMethods()
                        .stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.toSet())
                ))
                .grantType(String.join(",", registeredClient.getAuthorizationGrantTypes()
                        .stream()
                        .map(AuthorizationGrantType::getValue)
                        .collect(Collectors.toSet())
                ))
                .redirectUri(String.join(",", registeredClient.getRedirectUris()))
                .build();

        clientRepository.save(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        Client client = clientRepository.findById(Long.parseLong(id)).orElseThrow();
        return clientToRegisteredClient(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientRepository.findByClientId(clientId).orElseThrow();
        return clientToRegisteredClient(client);
    }

    public static RegisteredClient clientToRegisteredClient(Client client) {
        return RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientSecret(client.getSecret())
                .scopes(scopes -> {
                    scopes.addAll(Set.of(client.getScope().split(",")));
                })
                .clientAuthenticationMethods(methods -> methods
                        .addAll(Arrays.stream(client.getAuthMethod().split(","))
                                .map(ClientAuthenticationMethod::new)
                                .collect(Collectors.toSet())
                        ))
                .authorizationGrantTypes(grantTypes -> grantTypes
                        .addAll(Arrays.stream(client.getGrantType().split(","))
                                .map(AuthorizationGrantType::new)
                                .collect(Collectors.toSet())
                        ))
                .redirectUris(uris -> uris
                        .addAll(Set.of(client.getRedirectUri().split(","))))
                .build();
    }
}
