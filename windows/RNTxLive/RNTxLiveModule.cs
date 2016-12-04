using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Com.Reactlibrary.RNTxLive
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNTxLiveModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNTxLiveModule"/>.
        /// </summary>
        internal RNTxLiveModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNTxLive";
            }
        }
    }
}
