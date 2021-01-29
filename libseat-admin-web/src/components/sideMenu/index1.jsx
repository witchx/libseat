    import maxLogo from '@/assets/images/logo-min.jpg'
    import menu from './menu.json'

    export default {
        data() {
            return {
                activeName: this.$route.name,
                menu: menu,
                role: this.$store.getters.userInfo.role,
                theme: 'light',
                width:78,
                class:'menuitemClasses'
            }
        },
        render() {
            const menujsx = this.renderMenu(this.menu)
            return (
                <template>
                    <div className="layout">
                        <Layout>
                            <Sider ref="side1" hide-trigger collapsible collapsed-width={this.width} v-model="isCollapsed">
                                <Menu active-name={this.activeName} theme={this.theme} width="auto" class={this.class} open-names={['seat']}>
                                    <div class="logo-con">
                                        <img src={maxLogo} key="max-logo" />
                                    </div>
                                    {menujsx}
                                    <RadioGroup v-model={this.theme}>
                                        <Radio label="light"></Radio>
                                        <Radio label="dark"></Radio>
                                        <Radio label="primary"></Radio>
                                    </RadioGroup>
                                </Menu>
                            </Sider>
                            <Layout>
                                <Header style="{padding: 0}" class="layout-header-bar">
                                    <Icon onClick.native={this.collapsedSider()} class="rotateIcon" style="{margin: '0 20px'}" type="md-menu" size="24"></Icon>
                                </Header>
                                <Content style="{margin: '20px', background: '#fff', minHeight: '260px'}">
                                    Content
                                </Content>
                             </Layout>
                        </Layout>
                    </div>
                </template>
            )
        },
        methods: {
            renderMenu(menu) {
                return menu.map(item => {
                    if (item.children && item.children.length) {
                        return (
                            <Submenu name={item.name}>
                                <template slot="title">
                                    <Icon type={item.icon} />
                                    {item.title}
                                </template>
                                {this.renderMenu(item.children)}
                            </Submenu>
                        )
                    } else {
                        if (this.role === 2 && item.auth) {
                            return
                        } else {
                            return (
                                <MenuItem name={item.name} to={{path: item.path}}>
                                    <Icon type={item.icon}></Icon>
                                    <span>{item.title}</span>
                                </MenuItem>
                            )
                        }
                    }
                })
            },
            collapsedSider () {
                this.$refs.side1.toggleCollapse();
            }
        },
        watch: {
            '$route'(to, from) {
                this.activeName = to.name
            }
        }
    }
