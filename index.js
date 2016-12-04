import React, {PropTypes, Component} from 'react'
import ReactNative, {Platform, UIManager, requireNativeComponent, View} from 'react-native'

export default class TXLivePlayer extends Component {
  static propTypes = {
    source: PropTypes.string,
    renderRotation: PropTypes.number,
    renderMode: PropTypes.number,
    hardwareDecode: PropTypes.bool,
    mute: PropTypes.bool,
    onBegin: PropTypes.func,
    onLoading: PropTypes.func,
    onEnd: PropTypes.func,
    onProgress: PropTypes.func,
    onError: PropTypes.func,
    onDisconnect: PropTypes.func,
    ...View.propTypes
  }

  static defaultProps = {
    renderRotation: 0,
    renderMode: 1,
    hardwareDecode: false,
    mute: false
  }

  componentWillUnmount() {
    this.stop()
  }

  start() {
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(this),
      UIManager.RNTXLivePlayer.Commands.start,
      []
    )
  }

  pause() {
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(this),
      UIManager.RNTXLivePlayer.Commands.pause,
      []
    )
  }

  stop() {
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(this),
      UIManager.RNTXLivePlayer.Commands.stop,
      []
    )
  }

  mute(enable) {
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(this),
      UIManager.RNTXLivePlayer.Commands.mute,
      [enable]
    )
  }

  _handleBegin = (e) => {
    if (this.props.onBegin) {
      this.props.onBegin(e.nativeEvent)
    }
  }

  _handleEnd = (e) => {
    if (this.props.onEnd) {
      this.props.onEnd(e.nativeEvent)
    }
  }

  _handleProgress = (e) => {
    if (this.props.onProgress) {
      this.props.onProgress(e.nativeEvent)
    }
  }

  _handleError = (e) => {
    if (this.props.onError) {
      this.props.onError(e.nativeEvent)
    }
  }

  _handleLoading = (e) => {
    if (this.props.onLoading) {
      this.props.onLoading(e.nativeEvent)
    }
  }

  _handleDisconnect = (e) => {
    if (this.props.onDisconnect) {
      this.props.onDisconnect(e.nativeEvent)
    }
  }

  render() {
    const nativeProps = Object.assign({}, this.props, {
      onRNTXLivePlayerBegin: this._handleBegin,
      onRNTXLivePlayerEnd: this._handleEnd,
      onRNTXLivePlayerProgress: this._handleProgress,
      onRNTXLivePlayerError: this._handleError,
      onRNTXLivePlayerLoading: this._handleLoading,
      onRNTXLivePlayerDisconnect: this._handleDisconnect
    })
    return (
      <RNTXLivePlayer {...nativeProps}/>
    )
  }
}

const RNTXLivePlayer = requireNativeComponent('RNTXLivePlayer', {
  name: 'RNTXLivePlayer',
  propTypes: {
    source: PropTypes.string.isRequired,
    renderRotation: PropTypes.number.isRequired,
    renderMode: PropTypes.number.isRequired,
    hardwareDecode: PropTypes.bool.isRequired,
    mute: PropTypes.bool.isRequired,
    onRNTXLivePlayerBegin: PropTypes.func,
    onRNTXLivePlayerLoading: PropTypes.func,
    onRNTXLivePlayerEnd: PropTypes.func,
    onRNTXLivePlayerProgress: PropTypes.func,
    onRNTXLivePlayerError: PropTypes.func,
    onRNTXLivePlayerDisconnect: PropTypes.func,
    ...View.propTypes
  }
})
