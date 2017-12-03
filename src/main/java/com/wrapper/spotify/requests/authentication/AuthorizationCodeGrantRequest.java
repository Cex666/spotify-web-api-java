package com.wrapper.spotify.requests.authentication;

import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.*;
import com.wrapper.spotify.model_objects.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.AbstractRequest;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

public class AuthorizationCodeGrantRequest extends AbstractRequest {

  protected AuthorizationCodeGrantRequest(Builder builder) {
    super(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public SettableFuture<AuthorizationCodeCredentials> getAsync() {
    final SettableFuture<AuthorizationCodeCredentials> future = SettableFuture.create();

    try {
      final JsonObject jsonObject = new JsonParser().parse(postJson()).getAsJsonObject();
      future.set(new AuthorizationCodeCredentials.JsonUtil().createModelObject(jsonObject));
    } catch (Exception e) {
      future.setException(e);
    }

    return future;
  }

  public AuthorizationCodeCredentials get() throws
          IOException,
          NoContentException,
          BadRequestException,
          UnauthorizedException,
          ForbiddenException,
          NotFoundException,
          TooManyRequestsException,
          InternalServerErrorException,
          BadGatewayException,
          ServiceUnavailableException {
    final JsonObject jsonObject = new JsonParser().parse(postJson()).getAsJsonObject();
    return new AuthorizationCodeCredentials.JsonUtil().createModelObject(jsonObject);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    public Builder grantType(String grantType) {
      assert (grantType != null);
      return setBodyParameter("grant_type", grantType);
    }

    public Builder code(String code) {
      assert (code != null);
      return setBodyParameter("code", code);
    }

    public Builder redirectUri(String redirectUri) {
      assert (redirectUri != null);
      return setBodyParameter("redirect_uri", redirectUri);
    }

    public Builder basicAuthorizationHeader(String clientId, String clientSecret) {
      assert (clientId != null);
      assert (clientSecret != null);

      String idSecret = clientId + ":" + clientSecret;
      String idSecretEncoded = new String(Base64.encodeBase64(idSecret.getBytes()));

      return setHeaderParameter("Authorization", "Basic " + idSecretEncoded);
    }

    public AuthorizationCodeGrantRequest build() {
      setHost(Api.DEFAULT_AUTHENTICATION_HOST);
      setPort(Api.DEFAULT_AUTHENTICATION_PORT);
      setScheme(Api.DEFAULT_AUTHENTICATION_SCHEME);
      setPath("/api/token");

      return new AuthorizationCodeGrantRequest(this);
    }

  }
}
