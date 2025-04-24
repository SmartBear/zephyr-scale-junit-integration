#!/usr/bin/env bash

gpg --batch --gen-key <<EOF
Key-Type: 1
Key-Length: 2048
Subkey-Type: 1
Subkey-Length: 2048
Name-Real: zephyrscale
Name-Email: svc_zephyr_scale@smartbear.com
Expire-Date: 0
Passphrase: ${GPG_PASSPHRASE}
EOF

gpg --keyserver hkps://keys.openpgp.org --send-keys $(gpg --with-colons --fingerprint | awk -F: '$1 == "fpr" {print $10;}')