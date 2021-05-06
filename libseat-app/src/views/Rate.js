import React from 'react';
import Star from './Star';
import '../style/rate.scss'
export default class Rate extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 4,
      hoverValue: undefined,
    };
  }

  // 鼠标移出组件时，清空 hoverValue
  onMouseLeave = () => {
    this.setState({
      hoverValue: undefined,
    });
  };

  onClick = (event, index) => {
    this.onMouseLeave();
    this.setState({
      value: index + 1,
    });
  };

  onHover = (event, index) => {
    this.setState({
      hoverValue: index + 1,
    });
  };


  render() {
    const {
      count,
      className
    } = this.props;
    const { value, hoverValue } = this.state;
    const stars = [];
    this.props.parent.getStarNum(this.state.value)

    // 根据当前 value 生成所有星星
    for (let index = 0; index < count; index += 1) {
      stars.push(
        <Star
          key={index}
          index={index}
          count={count}
          value={hoverValue || value}
          onClick={this.onClick}
          onHover={this.onHover}
        />,
      );
    }

    return (
      <ul
        className={className}
        onMouseLeave={this.onMouseLeave}
        role="radiogroup"
      >
        {stars}
      </ul>
    );
  }
}
