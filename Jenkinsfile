stage('Sign JAR') {
    steps {
        sh '''
        set -euo pipefail

        SIGN_DIR="${WORKSPACE}/signed"
        mkdir -p "$SIGN_DIR"

        echo "=================================================="
        echo "🚀 SIGNING PROCESS STARTED"
        echo "Input File  : ${JAR_FILE}"
        echo "Project GUID: ${PROJECT_GUID}"
        echo "=================================================="

        codesign -guid ${PROJECT_GUID} \
                 -in "${JAR_FILE}" \
                 -out "$SIGN_DIR" \
                 -sign

        EXIT_CODE=$?

        echo "--------------------------------------------------"
        echo "Codesign Exit Code: $EXIT_CODE"
        echo "--------------------------------------------------"

        if [ $EXIT_CODE -ne 0 ]; then
            echo "❌ SIGNING FAILED"
            exit 1
        fi

        echo "✅ SIGN PROCESS COMPLETED SUCCESSFULLY"
        echo "=================================================="
        '''
    }
}

stage('Verify Signed JAR') {
    steps {
        sh '''
        set -euo pipefail

        SIGNED_FILE="${WORKSPACE}/signed/$(basename ${JAR_FILE})"

        echo "=================================================="
        echo "🔎 VERIFICATION PROCESS STARTED"
        echo "Signed File: $SIGNED_FILE"
        echo "=================================================="

        if [ ! -f "$SIGNED_FILE" ]; then
            echo "❌ Signed file not found"
            exit 1
        fi

        codesign -guid ${PROJECT_GUID} \
                 -out "$SIGNED_FILE" \
                 -verify

        EXIT_CODE=$?

        echo "--------------------------------------------------"
        echo "Verification Exit Code: $EXIT_CODE"
        echo "--------------------------------------------------"

        if [ $EXIT_CODE -ne 0 ]; then
            echo "❌ VERIFICATION FAILED"
            exit 1
        fi

        echo "✅ VERIFICATION COMPLETED SUCCESSFULLY"
        echo "=================================================="
        '''
    }
}
