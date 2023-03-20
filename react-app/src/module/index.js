export default class TagManager {

  constructor(
    bootstrap = '',
    serviceToken = '',
    events = ['click', 'mouseenter', 'mouseleave', 'scroll'],
    className = 'tag-manager'
  ) {

    if(!sessionStorage.getItem('TAGMANAGER_SESSION')){
      sessionStorage.setItem('TAGMANAGER_SESSION', 'test-session-id')
    }
    this.sessionId = sessionStorage.getItem('TAGMANAGER_SESSION')
    this.bootstrap = bootstrap;
    this.serviceToken = serviceToken;
    this.className = className;
    this.events = events;
    this.logStash = [];

    this.handleClick = function (e) {
      this.stackLog(e, 'click');
      this.flushLog();
    }
    this.handleMouseenter = function (e) {
      this.stackLog(e, 'mouseenter');
    }
    this.handleMouseleave = function (e) {
      this.stackLog(e, 'mouseenter');
    }
    this.handleScroll = function (e) {
      this.stackLog(e, 'mouseenter');
      this.flushLog();
    }

    this.flushLog = function() {
      fetch(this.bootstrap, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.logStash)
      })
      this.logStash = [];
    }.bind(this)

    this.stackLog = function(e, eventType='') {
      console.log("document.location.href : " + document.location.href)
      let body = {
        serviceToken: this.serviceToken,
        sessionId: this.sessionId,
        event: eventType,
        targetId: (e && e.target && e.target.id) ? e.target.id : 'none',
        positionX: e && e.pageX ? e.pageX : null,
        positionY: e && e.pageY ? e.pageY : null,
        location: document.location.href,
        timestamp: Date.now()
      }
      this.logStash.push(body)
    }.bind(this)

    this.eventDictionary = {
      'click': this.handleClick.bind(this),
      'mouseenter': this.handleMouseenter.bind(this),
      'mouseleave': this.handleMouseleave.bind(this),
      'scroll': this.handleScroll.bind(this),
    }
  }


  // className :

  attach() {
    let elements = document.querySelectorAll('.'+this.className);
    elements.forEach((elem) => {
      for (let i in this.events) {
        elem.addEventListener(this.events[i], this.eventDictionary[this.events[i]])
      }
    })
    this.stackLog(null, "pageenter");
    this.flushLog();
  }

  detach() {
    let elements = document.querySelectorAll('.'+this.className);
    elements.forEach((elem) => {
      for (let i in this.events) {
        elem.removeEventListener(this.events[i], this.eventDictionary[this.events[i]])
      }
    })
    this.stackLog(null, "pageleave");
    this.flushLog();
  }

}