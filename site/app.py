import logging
import os

import flask

DEBUG = os.getenv("DEBUG", "false") == "true"

PLAUSIBLE_SRC_URL = os.getenv("PLAUSIBLE_SRC_URL", "")
PLAUSIBLE_DATA_API = os.getenv("PLAUSIBLE_DATA_API", "")
PLAUSIBLE_DATA_DOMAIN = os.getenv("PLAUSIBLE_DATA_DOMAIN", "")

logging.basicConfig(
    level=logging.DEBUG if DEBUG else logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
)
logger = logging.getLogger(__name__)

app = flask.Flask(__name__)


@app.context_processor
def inject_plausible():
    return {
        "PLAUSIBLE_SRC_URL": PLAUSIBLE_SRC_URL,
        "PLAUSIBLE_DATA_API": PLAUSIBLE_DATA_API,
        "PLAUSIBLE_DATA_DOMAIN": PLAUSIBLE_DATA_DOMAIN,
    }


@app.route("/")
def index():
    return flask.render_template("index.html")


@app.route("/health")
def health():
    return flask.jsonify({"status": "healthy"}), 200


@app.errorhandler(404)
def page_not_found(e):
    return flask.render_template("404.html"), 404


if __name__ == "__main__":
    app.run(debug=DEBUG, port=5000)
