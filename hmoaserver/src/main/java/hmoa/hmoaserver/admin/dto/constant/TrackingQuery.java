package hmoa.hmoaserver.admin.dto.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrackingQuery {

    REGISTER_QUERY("mutation RegisterTrackWebhook($input: RegisterTrackWebhookInput!) { registerTrackWebhook(input: $input) }"),
    CHECK_QUERY("query Track(\n" +
            "  $carrierId: ID!,\n" +
            "  $trackingNumber: String!\n" +
            ") {\n" +
            "  track(\n" +
            "    carrierId: $carrierId,\n" +
            "    trackingNumber: $trackingNumber\n" +
            "  ) {\n" +
            "    lastEvent {\n" +
            "      time\n" +
            "      status {\n" +
            "        code\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}");

    private final String query;
}
