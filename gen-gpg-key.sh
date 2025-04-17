#!/usr/bin/env bash

if [[ -z "$GPG_PASSPHRASE" ]]; then
  echo "GPG_PASSPHRASE is not set. Please set it before running this script."
  exit 1
fi

gpg --batch --gen-key <<EOF
Key-Type: 1
Key-Length: 2048
Subkey-Type: 1
Subkey-Length: 2048
Name-Real: zephyrscale
Name-Email: tm4j.bot@gmail.com
Expire-Date: 0
Passphrase: $GPG_PASSPHRASE
EOF

FINGERPRINT=$(gpg --with-colons --fingerprint | awk -F: '$1 == "fpr" {print $10;}')
if [[ -z "$FINGERPRINT" ]]; then
  echo "No GPG key fingerprint found. Please check if the key was generated successfully."
  exit 1
fi
gpg --keyserver hkps://keys.openpgp.org --send-keys $FINGERPRINT