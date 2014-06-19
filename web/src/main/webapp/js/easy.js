
easy = {};

easy.Utils = {
  normalizeId: function(id) {
    if(id.indexOf("/") != 0) {
      return "/" + id;
    }
    else {
      return id;
    }
  },

  errorPage: function(code, text) {
    window.location.href = '/error.html?c=' + code + '&t=' + text;
  }
}

easy.ParentPage = function(id) {
  this.onContentReadyCallback = function() {}
  this.pageId = easy.Utils.normalizeId(id);
  var self = this;

  this.load = function(interval) {
      $.ajax( {
          url: "/srv/parentpage" + self.pageId,
          type: "GET",
          statusCode: {
              200: function(data) {
                  self.onContentReadyCallback(data)
              },
              404:  function(data) {
              }
          }
      });
  }

  this.onContentReady = function(callback) {
      self.onContentReadyCallback = callback
  }
}

easy.Subpages = function(id) {
  this.onContentReadyCallback = function() {}
  this.pageId = easy.Utils.normalizeId(id);
  var self = this;

  this.load = function(interval) {
      $.ajax( {
          url: "/srv/subpages" + self.pageId,
          type: "GET",
          statusCode: {
              200: function(data) {
                  self.onContentReadyCallback(data)
              }
          }
      });
  }

  this.onContentReady = function(callback) {
      self.onContentReadyCallback = callback
  }
}

 easy.Page = function(id) {
  this.refreshInterval = 1000
  this.text = "";
  this.status = "WAIT"
  this.lastStatus = this.status
  this.onContentReadyCallback = function() {}
  this.onStatusChangedCallback = function() {}

  this.pageId = easy.Utils.normalizeId(id);
  var self = this;

  this._refresh = function() {
      if(self.status == "CHANGED") {
          self._persist()
      }
      if(self.lastStatus != self.status) {
        self.lastStatus = self.status
        self._fireCurrentStatus()
      }
  }

  this._fireCurrentStatus = function() {
    self.onStatusChangedCallback(self.status)
  }

  this._persist = function() {
    self.status = "WAIT";
    $.ajax( {
        url: "/srv/page" + self.pageId,
        type: "POST",
        data: { content: self.text },
        success: function() {
          self.status =  "SAVED";
        }
    });
  }

  this.load = function() {
      self._fireCurrentStatus()
      $.ajax( {
          url: "/srv/page" + self.pageId,
          type: "GET",
          statusCode: {
              200: function(data) {
                  self.status = "SAVED"
                  self.text = data.content
                  self.onContentReadyCallback(data.content)
                  setInterval( self._refresh, self.refreshInterval)
              }
          },
          error: function(xhr, status, text) {
            easy.Utils.errorPage(xhr.status, text);
          }
      });
  }

  this.save = function(text) {
      if(self.text != text) {
          self.text = text;
          self.status = "CHANGED";
          self._fireCurrentStatus();
      }
  }

  this.onContentReady = function(callback) {
      self.onContentReadyCallback = callback
  }

  this.onStatusChanged = function(callback) {
      self.onStatusChangedCallback = callback
  }
}


